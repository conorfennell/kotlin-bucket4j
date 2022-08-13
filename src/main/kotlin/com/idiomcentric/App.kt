package com.idiomcentric

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import java.time.Duration

class BucketExamples {
    companion object {
        fun bandwidth() {
            val limit = Bandwidth.simple(1, Duration.ofSeconds(10L))
            val bucket = Bucket.builder().addLimit(limit).build()

            repeat(2) {
                println(bucket.tryConsume(1L))
            }
        }
    }
}

fun main() {
    BucketExamples.bandwidth()
}
