package kr.zziririt.zziririt.domain.member.repository

import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialMemberRepository : CrudRepository<SocialMemberEntity, Long> {

    fun findByEmail (email:String) : SocialMemberEntity?

}