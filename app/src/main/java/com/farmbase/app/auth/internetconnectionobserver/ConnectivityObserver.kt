package com.farmbase.app.auth.internetconnectionobserver

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    val isConnected: Flow<Boolean>
}