package kr.zziririt.zziririt.domain.report.model

import jakarta.persistence.*
import kr.zziririt.zziririt.domain.comment.model.CommentEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.post.model.PostEntity
import kr.zziririt.zziririt.global.entity.BaseEntity
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "report")
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
class ReportEntity(

    @ManyToOne
    @JoinColumn(name = "reporter_user", nullable = false)
    val reporterMember: SocialMemberEntity,

    @ManyToOne
    @JoinColumn(name = "reported_user", nullable = false)
    val reportedMember: SocialMemberEntity,

    @ManyToOne
    @JoinColumn(name = "reported_post_id", nullable = true)
    val reportedPostId: PostEntity? = null,

    @ManyToOne
    @JoinColumn(name = "reported_commnet_id", nullable = true)
    val reportedCommentId: CommentEntity? = null,

    @Column(name = "reported_reason", nullable = false)
    val reportedReason: String,

    @Column(name = "result", nullable = false)
    var result: Boolean = false

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    fun reportProcessed() {
        this.result = true
    }
}