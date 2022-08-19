package com.idiomcentric

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import java.time.Duration
import java.time.Instant
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

internal enum class PricingPlan {
    LOW {
        val limit: Bandwidth
            get() = Bandwidth.classic(20, Refill.intervally(20, Duration.ofHours(1)))
    },
    MEDIUM {
        val limit: Bandwidth
            get() = Bandwidth.classic(40, Refill.intervally(40, Duration.ofHours(1)))
    },
    HIGH {
        val limit: Bandwidth
            get() = Bandwidth.classic(100, Refill.intervally(100, Duration.ofHours(1)))
    },
    SPIKE {
        val limit: Bandwidth
            get() = Bandwidth.classic(1000, Refill.intervally(100, Duration.ofHours(1)))
    }
}
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

        fun refill() {
            val refill = Refill.intervally(10, Duration.ofMinutes(1))
            val limit = Bandwidth.classic(10, refill)
            val bucket: Bucket = Bucket.builder()
                .addLimit(limit)
                .build()

            repeat(10) {
                assert(bucket.tryConsume(1))
            }
            assert(!bucket.tryConsume(1))
        }

        fun refillGreedy() {
            val refill = Refill.greedy(10, Duration.ofMinutes(1))
            val limit = Bandwidth.classic(10, refill)
            val bucket: Bucket = Bucket.builder()
                .addLimit(limit)
                .build()

            repeat(10) {
                assert(bucket.tryConsume(1))
            }
            assert(!bucket.tryConsume(1))
        }
    }
}

fun main() {
    BucketExamples.bandwidth()
    BucketExamples.scheduleBandwidth()
    BucketExamples.refill()
    BucketExamples.refillGreedy()
}
