package kr.zziririt.zziririt.infra.cache

import kr.zziririt.zziririt.infra.cache.CacheType.ConstConfig.DEFAULT_MAX_SIZE
import kr.zziririt.zziririt.infra.cache.CacheType.ConstConfig.DEFAULT_TTL_SEC

enum class CacheType(
    val expireAfterWrite: Long,
    val maximumSize: Long
) {
    POST_SEARCH(DEFAULT_TTL_SEC, DEFAULT_MAX_SIZE);

    internal object ConstConfig {
        const val DEFAULT_TTL_SEC: Long = 3600
        const val DEFAULT_MAX_SIZE: Long = 10000
    }
}

