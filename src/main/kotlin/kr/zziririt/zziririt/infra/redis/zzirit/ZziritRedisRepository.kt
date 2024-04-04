package kr.zziririt.zziririt.infra.redis.zzirit

import kr.zziririt.zziririt.api.zzirit.dto.response.ZziritCountResponse
import kr.zziririt.zziririt.infra.querydsl.zzirit.dto.ZziritDto
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class ZziritRedisRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    val key = "postzzirit:ranking"
    fun updateRank(zziritRank: List<ZziritDto>) {

        redisTemplate.delete(key)
        //key, value 값으로 묶어 줍니다. (postId와 zziritCount)
        val rankHash = zziritRank.associate { it.postId.toString() to it.zziritCount }
        redisTemplate.opsForHash<String, Long>().putAll(key, rankHash)

        zziritRank.forEach {
            val infoKey = "zziritInfo:${it.postId}"
            redisTemplate.opsForHash<String, String>().putAll(
                infoKey, mapOf(
                    "postId" to it.postId.toString(),
                    "postTitle" to it.postTitle,
                    "boardUrl" to it.boardUrl,
                    "zziritCount" to it.zziritCount.toString(),
                    "boardId" to it.boardId.toString()
                )
            )
        }
    }

    fun findZziritPostRankingInRedis(): List<ZziritCountResponse>? {
        val rankings = redisTemplate.opsForHash<String, Long>().entries(key)
            ?: return emptyList()

        return rankings.map { (postId, zziritCount) ->
            val infoKey = "zziritInfo:$postId"
            val details = redisTemplate.opsForHash<String, String>().entries(infoKey)

            ZziritCountResponse(
                postId = details["postId"]?.toLong() ?: 0L,
                zziritCount = details["zziritCount"]?.toLong() ?: 0L,
                postTitle = details["postTitle"] ?: "",
                boardUrl = details["boardUrl"] ?: "",
                boardId = details["boardId"]?.toLong() ?: 0L,
            )
        }
    }
}