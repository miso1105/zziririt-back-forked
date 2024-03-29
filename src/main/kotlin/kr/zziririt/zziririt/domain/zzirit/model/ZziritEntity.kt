package kr.zziririt.zziririt.domain.zzirit.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.post.model.PostEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "zzirit")
@SQLDelete(sql = "UPDATE zzirit SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class ZziritEntity(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member")
    val socialMember: SocialMemberEntity,

    @Column(name = "entity_id")
    val entityId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    var zziritEntityType: ZziritEntityType,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(name = "isZzirit", nullable = false)
    var isZzirit: Boolean = true

    fun toggleZzirit(): Boolean {
        isZzirit = !isZzirit
        return isZzirit
    }
}