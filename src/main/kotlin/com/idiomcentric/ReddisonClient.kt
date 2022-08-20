package com.idiomcentric

import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy
import io.github.bucket4j.distributed.proxy.AsyncProxyManager
import io.github.bucket4j.redis.redisson.cas.RedissonBasedProxyManager
import org.redisson.command.CommandSyncService
import org.redisson.config.Config
import org.redisson.config.ConfigSupport
import java.time.Duration

class ReddisonClient {
    companion object {
        fun syncClient(): RedissonBasedProxyManager {
            val config = Config()
            config.useSingleServer() // use "rediss://" for SSL connection
                .address = "redis://127.0.0.1:6379"
            val connectionManager = ConfigSupport.createConnectionManager(Config(config))

            val commandExecutor = CommandSyncService(connectionManager, null)
            return RedissonBasedProxyManager
                .builderFor(commandExecutor)
                .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10)))
                .build()
        }

        fun asyncClient(): AsyncProxyManager<String> {
            val config = Config()
            config.useSingleServer() // use "rediss://" for SSL connection
                .address = "redis://127.0.0.1:6379"
            val connectionManager = ConfigSupport.createConnectionManager(Config(config))

            val commandExecutor = CommandSyncService(connectionManager, null)
            return RedissonBasedProxyManager
                .builderFor(commandExecutor)
                .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(10)))
                .build().asAsync()
        }
    }
}
