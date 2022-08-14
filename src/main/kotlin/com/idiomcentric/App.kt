package com.idiomcentric

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import java.time.Duration
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class BucketExamples {
    companion object {
        private val scheduleExecutor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
        fun bandwidth() {
            val limit = Bandwidth.simple(1, Duration.ofSeconds(10L))
            val bucket = Bucket.builder().addLimit(limit).build()

            repeat(2) {
                println(bucket.tryConsume(1L))
            }
        }

        fun scheduleBandwidth() {
            val limit = Bandwidth.simple(1, Duration.ofSeconds(5L))
            val bucket = Bucket.builder().addLimit(limit).build()

            val scheduler = bucket.asScheduler()

            repeat(2) {
                // Consume a token from the token bucket.
                // If a token is not available this method will block until the refill adds one to the bucket.
                val token = scheduler.consume(1L, scheduleExecutor)
                token.get()
                println(Instant.now())
            }
            scheduleExecutor.shutdown()
        }
    }
}

fun main() {
    BucketExamples.bandwidth()
    BucketExamples.scheduleBandwidth()
}
