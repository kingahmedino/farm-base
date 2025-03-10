package com.farmbase.app.database.couchbase

import android.content.Context
import android.util.Log
import com.couchbase.lite.CouchbaseLite
import com.couchbase.lite.Database
import java.util.concurrent.atomic.AtomicReference

class DBManager {

    // <.>
    // One-off initialization
    private fun init(context: Context) {
        CouchbaseLite.init(context)
        Log.i(TAG, "CBL Initialized")

    }

    // <.>
    // Create a database
    fun getDatabase(): Database {
        val database = Database("farmbaseDB")
        Log.i(TAG, "Database created")

        return database
    }

    companion object {
        private const val TAG = "DBManager"

        private val INSTANCE = AtomicReference<DBManager?>()

        @Synchronized
        fun getInstance(context: Context): DBManager {
            var mgr = INSTANCE.get()
            if (mgr == null) {
                mgr = DBManager()
                if (INSTANCE.compareAndSet(null, mgr)) {
                    mgr.init(context)
                }
            }
            return INSTANCE.get()!!
        }
    }
}
