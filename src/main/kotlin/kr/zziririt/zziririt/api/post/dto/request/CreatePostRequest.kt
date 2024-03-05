package kr.zziririt.zziririt.api.post.dto.request

import jakarta.validation.constraints.Size
import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity
import kr.zziririt.zziririt.domain.post.model.PostEntity
import org.springframework.validation.annotation.Validated

@Validated
data class CreatePostRequest(
    @field:Size(min = 1, max = 255, message = "게시글 제목의 글자수는 {min}자 이상 {max} 자 이하여야 합니다.")
    val title: String,
    @field:Size(min = 1, max = 20000, message = "게시글 내용의 글자수는 {min}자 이상 {max} 자 이하여야 합니다.")
    val content: String,
    val privateStatus: Boolean
) {
    lateinit var board: BoardEntity
    lateinit var member: SocialMemberEntity

    fun board(board: BoardEntity): CreatePostRequest {
        this.board = board
        return this
    }

    fun member(member: SocialMemberEntity): CreatePostRequest {
        this.member = member
        return this
    }

    fun toEntity(): PostEntity = PostEntity(
        board = board,
        socialMember = member,
        title = title,
        content = content,
        privateStatus = privateStatus
    )
}