package com.personal.animeshpandey.agrisecure.Data.bluetooth.BLE

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import com.personal.animeshpandey.agrisecure.Data.bluetooth.ConnectionState
import com.personal.animeshpandey.agrisecure.Data.bluetooth.ReadingModel
import com.personal.animeshpandey.agrisecure.Data.bluetooth.SensorDataManager
import com.personal.animeshpandey.agrisecure.Util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@SuppressLint("MissingPermission")
class SensorDataManagerImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter,
    private val context: Context
) :SensorDataManager {

    private val DEVICE_NAME = "AgriSecureFarmModule"
    private val TEMP_HUMIDITY_SERVICE_UIID = "5f7d4fc1-75f1-4569-bcf1-11a7b4dda674"
    private val TEMP_HUMIDITY_CHARACTERISTICS_UUID = "0d3b0353-b26a-44e2-9413-d104b8af0360"

    override val data: MutableSharedFlow<Resource<ReadingModel>> = MutableSharedFlow()

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings = ScanSettings.Builder()
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    private var gatt: BluetoothGatt? = null

    private var isScanning = false

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val scanCallback = object : ScanCallback(){

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if(result.device.name == DEVICE_NAME){
                Log.d("DEVICE DETECTED CORRECTLY","Farm Module discovered")
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Connecting to device..."))
                }
                if(isScanning){
                    result.device.connectGatt(context,false, gattCallback,BluetoothDevice.TRANSPORT_LE)
                    isScanning = false
                    bleScanner.stopScan(this)
                }
            }
        }
    }

    private var currentConnectionAttempt = 1
    private var MAXIMUM_CONNECTION_ATTEMPTS = 5

    private val gattCallback = object : BluetoothGattCallback(){
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                Log.d("Bluetooth Connection status","DEVICE CONNECTED")
                if(newState == BluetoothProfile.STATE_CONNECTED){
                    coroutineScope.launch {
                        data.emit(Resource.Loading(message = "Connected to Farm Module, Discovering readings..."))
                    }
                    gatt.discoverServices()
                    this@SensorDataManagerImpl.gatt = gatt
                } else if(newState == BluetoothProfile.STATE_DISCONNECTED){
                    coroutineScope.launch {
                        data.emit(Resource.Success(data = ReadingModel(-1,-1,-1,-1, ConnectionState.Disconnected)))
                    }
                    gatt.close()
                }
            }else{
                gatt.close()
                currentConnectionAttempt+=1
                coroutineScope.launch {
                    Log.d("BLUETOOTH", "Cannot Discover the Service,TRYING AGAIN")
                    data.emit(
                        Resource.Loading(
                            message = "Attempting to connect $currentConnectionAttempt/$MAXIMUM_CONNECTION_ATTEMPTS"
                        )
                    )
                }
                if(currentConnectionAttempt<=MAXIMUM_CONNECTION_ATTEMPTS){
                    startReceiving()
                }else{
                    coroutineScope.launch {
                        data.emit(Resource.Error(errorMessage = "Could not connect to Farm Module"))
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            with(gatt){
                printGattTable()
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Adjusting MTU space..."))
                }
                gatt.requestMtu(400)//400 Bytes maximum packet length
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            val characteristic = findCharacteristics(TEMP_HUMIDITY_SERVICE_UIID, TEMP_HUMIDITY_CHARACTERISTICS_UUID)
            if(characteristic == null){
                Log.d("BLUETOOTH", "Cannot Discover the Service")
                coroutineScope.launch {
                    data.emit(Resource.Error(errorMessage = "Could not find Sensor Publisher"))
                }
                return
            }
            enableNotification(characteristic)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            with(characteristic){
                when(uuid){
                    UUID.fromString(TEMP_HUMIDITY_CHARACTERISTICS_UUID) -> {
                        //XX XX XX XX XX XX
//                        val multiplicator = if(value.first().toInt()> 0) -1 else 1
//                        val temperature = value[1].toInt() + value[2].toInt() / 10f
//                        val humidity = value[4].toInt() + value[5].toInt() / 10f
//                        val tempHumidityResult = TempHumidityResult(
//                            multiplicator * temperature,
//                            humidity,
//                            ConnectionState.Connected
//                        )
                        //recv packet is in the form of a byte array

                        var recv_array = value
                        var recv_String = ""

                        if(recv_array==null){
                            coroutineScope.launch {
                                data.emit(
                                    Resource.Loading(data = null, message = "Still fetching!")
                                )
                            }
                        }else{
                            for(i in recv_array){
                                recv_String = recv_String + i.toInt().toChar()
                            }
                            Log.d("BLE PACKET",recv_String)
                        }

                        var temperature: Int = 0
                        var humidity: Int = 0
                        var rain: Int = 0
                        var soil: Int = 0

                        val values = recv_String.split("\\s+".toRegex())

                        for (i in values.indices) {
                            when {
                                values[i] == "t:" -> temperature = values[i + 1].toInt()
                                values[i] == "h:" -> humidity = values[i + 1].toInt()
                                values[i] == "r:" -> rain = values[i + 1].toInt()
                                values[i] == "s:" -> soil = values[i + 1].toInt()
                            }
                        }

                        val resultPacket = ReadingModel(temperature,humidity,soil,rain,ConnectionState.Connected)

                        coroutineScope.launch {
                            data.emit(
                                Resource.Success(data = resultPacket)
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }


    }



    private fun enableNotification(characteristic: BluetoothGattCharacteristic){
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID) //check if Your CCCD Descriptor is same
        val payload = when {
            characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            else -> return
        }

        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if(gatt?.setCharacteristicNotification(characteristic, true) == false){
                Log.d("BLEReceiveManager","set characteristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, payload)
        }
    }

    private fun writeDescription(descriptor: BluetoothGattDescriptor, payload: ByteArray){
        gatt?.let { gatt ->
            descriptor.value = payload
            gatt.writeDescriptor(descriptor)
        } ?: error("Not connected to the Farm Module")
    }

    private fun findCharacteristics(serviceUUID: String, characteristicsUUID:String):BluetoothGattCharacteristic?{
        return gatt?.services?.find { service ->
            service.uuid.toString() == serviceUUID
        }?.characteristics?.find { characteristics ->
            characteristics.uuid.toString() == characteristicsUUID
        }
    }

    override fun startReceiving() {
        coroutineScope.launch {
            data.emit(Resource.Loading(message = "Scanning for Farm Module..."))
        }
        isScanning = true
        bleScanner.startScan(null,scanSettings,scanCallback)
    }

    override fun reconnect() {
        gatt?.connect()
    }

    override fun disconnect() {
        gatt?.disconnect()
    }



    override fun closeConnection() {
        bleScanner.stopScan(scanCallback)
        val characteristic = findCharacteristics(TEMP_HUMIDITY_SERVICE_UIID, TEMP_HUMIDITY_CHARACTERISTICS_UUID)
        if(characteristic != null){
            disconnectCharacteristic(characteristic)
        }
        gatt?.close()
    }

    private fun disconnectCharacteristic(characteristic: BluetoothGattCharacteristic){
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if(gatt?.setCharacteristicNotification(characteristic,false) == false){
                Log.d("TempHumidReceiveManager","set charateristics notification failed")
                return
            }
            writeDescription(cccdDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
        }
    }

}