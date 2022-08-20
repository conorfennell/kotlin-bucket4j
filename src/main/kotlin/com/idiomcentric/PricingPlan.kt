package com.idiomcentric

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Refill
import java.time.Duration

enum class PricingPlan {
    LOW {
        override val tokens: Long = 1000
    },
    MEDIUM {
        override val tokens: Long = 1000
    },
    HIGH {
        override val tokens: Long = 1000
    },
    SPIKE {
        override val tokens: Long = 1000
    };
    abstract val tokens: Long
    private val period: Duration = Duration.ofSeconds(60)
    private val smoothPeriod: Duration = Duration.ofSeconds(5)
    val bandwidths: Collection<Bandwidth>
        get() {
            val smoothTokens = ((tokens / (period.seconds)) * smoothPeriod.seconds) * 5
            return setOf(
                Bandwidth.classic(tokens, Refill.intervally(tokens, period)),
                Bandwidth.classic(
                    smoothTokens,
                    Refill.greedy(smoothTokens, smoothPeriod)
                )
            )
        }
}
