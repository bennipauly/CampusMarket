package de.dhbw.campusmarket.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

// Asks Android whether the device currently has an internet connection.
// Needs the permission ACCESS_NETWORK_STATE (a normal permission, no dialog for the user).
// Created exactly once in CampusMarketApplication and only used through the repository.
class ConnectionChecker(context: Context) {

    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    // false in airplane mode or without Wi-Fi/mobile data: then there is no active network
    // or it cannot reach the internet.
    fun isOnline(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
