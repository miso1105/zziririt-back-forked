package kr.zziririt.zziririt.domain.post.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class PostEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    var board: BoardEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_member_id", nullable = false)
    val socialMember: SocialMemberEntity,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "private_status", nullable = false)
    var privateStatus: Boolean = false,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "blind_status", nullable = false)
    var blindStatus: Boolean = false

    @Column(name = "zzirit_count", nullable = false)
    var zziritCount: Long = 0L

    @Column(name = "hit", nullable = false)
    var hit: Long = 0L

    fun update(title: String, content: String) {
        this.title = title
        this.content = content
    }

    fun incrementZzirit() = this.zziritCount++

    fun incrementHit() = this.hit++

    fun togglePrivateStatus() = !this.privateStatus

    fun toggleBlindStatus() = !this.privateStatus
}