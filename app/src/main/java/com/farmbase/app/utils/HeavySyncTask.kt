package com.farmbase.app.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.sin
import kotlin.random.Random
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Simulates a heavy sync task with configurable CPU, memory, and network load
 */
class HeavySyncTask(
    private val tableName: String,
    private val taskConfig: TaskConfig
) {
    data class TaskConfig(
        val networkLatencyMs: LongRange = 500L..3000L,  // Simulate network latency
        val failureRate: Float = 0.05f,                 // Occasional failures (5%)
        val cpuIntensity: Float = 0.7f,                 // 0-1 scale of CPU load
        val memoryUsage: Int = 50 * 1024 * 1024,        // Memory usage in bytes
        val logProgress: Boolean = true                  // Whether to log progress
    )

    /**
     * Executes the heavy sync task for the given table
     * Returns the success status and number of records processed
     */
    suspend fun execute(recordTitle: String): Pair<Boolean, Int> = withContext(Dispatchers.IO) {
        try {
            // Log start of sync
            if (taskConfig.logProgress) {
                log("Starting sync for: $tableName - $recordTitle")
            }

            // Simulate network latency for initial connection
            val initialLatency = Random.nextLong(
                taskConfig.networkLatencyMs.first,
                taskConfig.networkLatencyMs.last
            )
            delay(initialLatency)

            // Simulate occasional random failure
            if (Random.nextFloat() < taskConfig.failureRate) {
                if (taskConfig.logProgress) {
                    log("❌ Network failure during sync of $tableName")
                }
                return@withContext Pair(false, 0)
            }

            val batchSize = 1000

            // Simulate CPU-intensive work
            if (taskConfig.cpuIntensity > 0) {
                performCpuIntensiveWork(taskConfig.cpuIntensity, batchSize)
            }

            // Simulate network latency for each batch
            val batchLatency = Random.nextLong(
                taskConfig.networkLatencyMs.first / 2,
                taskConfig.networkLatencyMs.last / 2
            )
            delay(batchLatency)

            // Final network latency for committing changes
            val finalLatency = Random.nextLong(
                taskConfig.networkLatencyMs.first,
                taskConfig.networkLatencyMs.last
            )
            delay(finalLatency)

            if (taskConfig.logProgress) {
                log("✅ Completed sync for $tableName: $recordTitle")
            }

            return@withContext Pair(true, 0)
        } catch (e: Exception) {
            log("❌ Error syncing $tableName: ${e.message}")
            return@withContext Pair(false, 0)
        }
    }

    /**
     * Simulates CPU-intensive computation
     */
    private fun performCpuIntensiveWork(intensity: Float, iterations: Int) {
        val actualIterations = (iterations * intensity * 1000).toInt()
        var result = 0.0

        for (i in 0 until actualIterations) {
            // Perform some meaningless but CPU-intensive math
            result += sin(i.toDouble()) * cos(i.toDouble() * 1.5)

            // Add some complex math operations
            result = result.pow(1.01) + sqrt(result.absoluteValue + 1.0)

            // Prevent compiler optimization
            if (result == Double.NEGATIVE_INFINITY) {
                println("This will never happen but prevents optimization")
            }
        }
    }

    private fun log(message: String) {
        println("[${System.currentTimeMillis()}] $message")
    }
}
