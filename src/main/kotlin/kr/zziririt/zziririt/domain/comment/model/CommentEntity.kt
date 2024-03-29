package kr.zziririt.zziririt.domain.comment.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.post.model.PostEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "comment")
@SQLDelete(sql = "UPDATE comment SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class CommentEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    var post: PostEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "social_member_id", nullable = false)
    val socialMember: SocialMemberEntity,

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "private_status", nullable = false)
    var privateStatus: Boolean = false,

    @Column(name = "zzirit_count", nullable = false)
    var zziritCount: Long = 0L,
) : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun update(content: String) {
        this.content = content
    }

    fun incrementZzirit() = this.zziritCount++

    fun decrementZzirit() = this.zziritCount--

    fun togglePrivateStatus() {
        this.privateStatus = !privateStatus
    }
}