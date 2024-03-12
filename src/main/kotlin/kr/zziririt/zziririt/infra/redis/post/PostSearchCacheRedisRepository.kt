package kr.zziririt.zziririt.infra.redis.post

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.data.redis.core.ListOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

private val kLogger = KotlinLogging.logger {}

@Repository
class PostSearchCacheRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    lateinit var listOperations: ListOperations<String, String>

    @PostConstruct
    fun init() {
        listOperations = redisTemplate.opsForList()
    }

    internal object ConstCacheConfig {
        const val POST_SEARCH_TERM = "POST_SEARCH::ZziriritCache_"
    }

    fun saveSearchPostCacheKeyByPostId(postId: Long, searchPostCacheKey: String) {
        kLogger.debug { "[+] saveSearchPostCacheKeyByPostId Start !!!" }
        val key = "${ConstCacheConfig.POST_SEARCH_TERM}PostId:$postId"
        println(key)

        listOperations.leftPush(key, searchPostCacheKey)
    }

    fun clearAllSearchPostCacheRelatedToPostId(postId: Long) {
        kLogger.debug { "[+] clearAllSearchPostCacheRelatedToPostId Start !!!" }
        val key = "${ConstCacheConfig.POST_SEARCH_TERM}PostId:$postId"
        val size = listOperations.size(key)?.let{listOperations.size(key)} ?: 0

        listOperations.range(key, 0, size)?.forEach { searchPostCacheKey ->
            redisTemplate.delete(searchPostCacheKey)
        }

        redisTemplate.delete(key)
    }
}