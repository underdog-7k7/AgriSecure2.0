package com.personal.animeshpandey.agrisecure.presentation

sealed class Screen(val route:String){
    object LoginScreen:Screen("login_screen")
    object MainScreen:Screen("Main_screen")
    object ContractScreen:Screen("contract_screen")
    object RaiseClaimScreen:Screen("raise_claim_screen")
}