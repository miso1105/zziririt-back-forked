package kr.zziririt.zziririt.infra.cache

import com.github.benmanes.caffeine.cache.Caffeine
import io.github.oshai.kotlinlogging.KotlinLogging
import kr.zziririt.zziririt.api.post.dto.PostSearchCondition
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.cache.interceptor.SimpleKey
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.lang.reflect.Method
import java.util.concurrent.TimeUnit

private val kLogger = KotlinLogging.logger {}

@EnableCaching
@Configuration
class CacheConfig {
    @Bean
    fun caffeineCacheList(): List<CaffeineCache> {
        return CacheType.entries.map { type ->
            Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(type.expireAfterWrite, TimeUnit.SECONDS)
                .maximumSize(type.maximumSize)
                .build<Any, Any>()
                .let { CaffeineCache(type.name, it) }
        }
    }

    @Bean
    fun caffeineCacheManager(caffeineCacheList: List<CaffeineCache>): CacheManager {
        kLogger.debug { "[+] caffeineCacheManager Start !!!" }
        return SimpleCacheManager()
            .apply { this.setCaches(caffeineCacheList) }
    }

    @Bean
    @Primary
    fun redisCacheManager(
        redisConnectionFactory: RedisConnectionFactory
    ): CacheManager {
        kLogger.debug { "[+] redisCacheManager Start !!!" }
        val config = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    JdkSerializationRedisSerializer()
                )
            )
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory)
            .withInitialCacheConfigurations(CacheType.entries.associate { it.name to config })
            .build()
    }

    class PostSearchKeyGenerator : KeyGenerator {
        override fun generate(target: Any, method: Method, vararg params: Any): Any {
            return if (params.isEmpty()) {
                SimpleKey.EMPTY
            } else if (params.size == 2) {
                val boardId = params[0]
                val postSearchCondition = params[1] as PostSearchCondition
                val sb = StringBuilder()
                val baseKey = "ZziriritCache"
                sb.append(baseKey)
                sb.append("_boardId:${boardId}")
                if (postSearchCondition.searchType != null) {
                    sb.append("_Type:${postSearchCondition.searchType}")
                }
                if (postSearchCondition.searchTerm != null) {
                    sb.append("_Term:${postSearchCondition.searchTerm}")
                }
                sb.append("_Page:${postSearchCondition.page}")
                sb.append("_Size:${postSearchCondition.size}")
                if (postSearchCondition.categoryId != null) {
                    sb.append("_categoryId:${postSearchCondition.categoryId}")
                }
                return sb.toString()
            } else {
                return SimpleKey("ZziriritCache_", *params)
            }
        }
    }

    @Bean(name = ["PostSearchKeyGenerator"])
    fun postSearchKeyGenerator(): KeyGenerator = PostSearchKeyGenerator()
}