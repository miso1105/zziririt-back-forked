package kr.zziririt.zziririt.api.zzirit.service

import kr.zziririt.zziririt.api.zzirit.dto.response.ZziritCountResponse
import kr.zziririt.zziririt.domain.zzirit.repository.ZziritRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ZziritService (
    private val zziritRepository: ZziritRepository
) {
    @Scheduled(cron = "0 0 0/1 1/1 * ?")
    fun updateRank(){
        val range = LocalDateTime.now().minusDays(1)
        val ranking = zziritRepository.findZziritRankInPosts(range)
        zziritRepository.updateRank(ranking)
    }

    fun findZziritRanking() : List<ZziritCountResponse>? {

        var rankingInRedis = zziritRepository.findZziritPostRankingInRedis()

        if(rankingInRedis==null || rankingInRedis.isEmpty()) {
            val range = LocalDateTime.now().minusDays(1)
            val ranking = zziritRepository.findZziritRankInPosts(range)
            zziritRepository.updateRank(ranking)
            rankingInRedis = zziritRepository.findZziritPostRankingInRedis()
        }

        return rankingInRedis
    }
}