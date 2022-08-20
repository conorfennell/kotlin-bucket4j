package com.idiomcentric

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.BucketConfiguration
import io.github.bucket4j.ConfigurationBuilder
import io.github.bucket4j.distributed.proxy.AsyncProxyManager
import io.github.bucket4j.distributed.proxy.ProxyManager
import kotlinx.coroutines.future.await
import java.util.UUID

fun ConfigurationBuilder.addBandwidths(bandwidths: Collection<Bandwidth>): ConfigurationBuilder {
    bandwidths.forEach { bandwidth -> this.addLimit(bandwidth) }
    return this
}

class AsyncRateLimiter(private val proxyManager: AsyncProxyManager<String>) {
    suspend fun isLimited(id: UUID, tokens: Long): Boolean {
        val configuration: BucketConfiguration = BucketConfiguration.builder()
            .addBandwidths(PricingPlan.LOW.bandwidths)
            .build()

        val bucket = proxyManager.builder().build(id.toString(), configuration)

        return !bucket.tryConsume(tokens).await()
    }
}

class SyncRateLimiter(private val proxyManager: ProxyManager<String>) {
    fun isLimited(id: UUID, tokens: Long): Boolean {
        val configuration: BucketConfiguration = BucketConfiguration.builder()
            .addBandwidths(PricingPlan.SPIKE.bandwidths)
            .build()

        val bucket = proxyManager.builder().build(id.toString(), configuration)

        return bucket.tryConsume(tokens)
    }
}
