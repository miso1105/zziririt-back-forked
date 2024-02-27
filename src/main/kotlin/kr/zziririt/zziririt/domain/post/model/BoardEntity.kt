package kr.zziririt.zziririt.domain.post.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "board")
@SQLDelete(sql = "UPDATE board SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class BoardEntity(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    val parent: BoardEntity?,

    @BatchSize(size = 10)
    @OneToOne(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
    val child: BoardEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_member_id", nullable = false)
    val socialMember: SocialMemberEntity,

    @Column(name = "board_name", nullable = false)
    var boardName: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val categoryEntity: CategoryEntity,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}