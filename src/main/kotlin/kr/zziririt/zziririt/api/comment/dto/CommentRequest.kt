package kr.zziririt.zziririt.api.comment.dto

import jakarta.validation.constraints.Size
import kr.zziririt.zziririt.domain.comment.model.CommentEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.post.model.PostEntity
import org.springframework.validation.annotation.Validated

@Validated
data class CommentRequest (
    @field:Size(min = 1, max = 500, message = "댓글의 글자 수는 {min}자 이상 {max}자 이하여야 합니다.")
    val content: String,
    val privateStatus: Boolean
) {
    fun to(socialMemberEntity: SocialMemberEntity, postEntity: PostEntity) = CommentEntity(
        content = content,
        privateStatus = privateStatus,
        socialMember = socialMemberEntity,
        post = postEntity
    )
}