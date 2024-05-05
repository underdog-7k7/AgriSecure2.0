package com.personal.animeshpandey.agrisecure.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navigation(
    onBluetoothStateChanged:()->Unit
) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.LoginScreen.route){
        composable(Screen.LoginScreen.route){
            Login(navController)
        }

        composable(Screen.MainScreen.route){
            MainScreen(onBluetoothStateChanged = onBluetoothStateChanged,navController=navController)
        }

        composable(Screen.ContractScreen.route){
            ContractScreen(navController)
        }

        composable(Screen.RaiseClaimScreen.route){
            RaiseClaim(navController)
        }
    }

}

