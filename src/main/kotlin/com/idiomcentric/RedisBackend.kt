package com.idiomcentric

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.util.UUID

fun main() {
    val reddisonClient = ReddisonClient.asyncClient()
    val rateLimiter = AsyncRateLimiter(reddisonClient)
    val constantUser = UUID.fromString("4ab6e8ac-87d9-480a-8e62-9994fd10b15b")

    runBlocking(Dispatchers.IO) {
        repeat(10) { run ->
            launch {
                var limited = 0L
                var nonLimited = 0L
                val user = UUID.randomUUID()
                repeat(10000) {
                    if (rateLimiter.isLimited(user, 1)) {
                        limited++
                    } else {
                        nonLimited++
                    }

                    if (it % 100 == 0) {
                        println("run=$run limited=$limited non-limited=$nonLimited time=${Instant.now()}")
                    }
                }
            }
        }
    }
}
