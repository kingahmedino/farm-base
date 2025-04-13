package com.farmbase.app.auth.koin

import com.farmbase.app.auth.datastore.model.StartDestinationInterface
import com.farmbase.app.auth.datastore.repo.StartDestinationRepo
import com.farmbase.app.auth.datastore.viewmodel.StartDestinationViewModel
import com.farmbase.app.auth.internetconnectionobserver.AndroidConnectivityObserver
import com.farmbase.app.auth.internetconnectionobserver.ConnectivityObserver
import com.farmbase.app.auth.sessionManager.SessionManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


// ConnectivitySessionModule.kt
val connectivitySessionModule = module {

    single {
        SessionManager(context = get())
    }

    single<ConnectivityObserver> {
        AndroidConnectivityObserver(context = get())
    }

    // Provide Start Destination Repository
//    single<StartDestinationInterface> {
//        StartDestinationRepo(get())
//    }

    single {
        StartDestinationRepo(get())
    }

}
val viewModelModule = module {

    viewModel {
        StartDestinationViewModel(get()) // Here, android context can be used to inject application class
    }
}

