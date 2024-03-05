package kr.zziririt.zziririt.infra.jpa.post

import kr.zziririt.zziririt.domain.post.model.PostEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PostJpaRepository : JpaRepository<PostEntity, Long> {
}