package kr.zziririt.zziririt.api.comment.service

import kr.zziririt.zziririt.api.comment.dto.CommentRequest
import kr.zziririt.zziririt.api.comment.dto.CommentResponse
import kr.zziririt.zziririt.api.post.dto.response.ZziritStatusResponse
import kr.zziririt.zziririt.domain.comment.repository.CommentRepository
import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.domain.post.repository.PostRepository
import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntity
import kr.zziririt.zziririt.domain.zzirit.model.ZziritEntityType
import kr.zziririt.zziririt.domain.zzirit.repository.ZziritRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class CommentService(
    private val postRepository: PostRepository,
    private val socialMemberRepository: SocialMemberRepository,
    private val commentRepository: CommentRepository,
    private val zziritRepository: ZziritRepository
) {
    fun createComment(postId: Long, commentRequest: CommentRequest, userPrincipal: UserPrincipal) {
        val findSocialMember =
            socialMemberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(
                ErrorCode.MODEL_NOT_FOUND
            )
        val findPost = postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val saveComment = commentRequest.to(findSocialMember, findPost)

        commentRepository.save(saveComment)
    }

    @Transactional
    fun updateComment(postId: Long, commentId: Long, commentRequest: CommentRequest, userPrincipal: UserPrincipal) {
        socialMemberRepository.findByIdOrNull(userPrincipal.memberId) ?: throw ModelNotFoundException(
            ErrorCode.MODEL_NOT_FOUND
        )
        postRepository.findByIdOrNull(postId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findComment =
            commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        if (commentRequest.privateStatus != findComment.privateStatus) {
            findComment.togglePrivateStatus()
        }
        findComment.update(content = commentRequest.content)
    }

    fun deleteComment(commentId: Long, userPrincipal: UserPrincipal) {
        socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val findComment =
            commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        commentRepository.delete(findComment)
    }

    fun getComments(userPrincipal: UserPrincipal?, postId: Long): List<CommentResponse>? {
        val findComments = commentRepository.findAllByPostId(postId)

        return if (userPrincipal != null) {
            val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
                ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

            findComments?.map {
                var permissionToUpdateStatus = false
                var permissionToDeleteStatus = false
                if (it.socialMember.id == userPrincipal.memberId) {
                    permissionToUpdateStatus = true
                    permissionToDeleteStatus = true
                } else if (findSocialMember.memberRole == MemberRole.ADMIN || userPrincipal.memberId == it.post.board.id) {
                    permissionToDeleteStatus = true
                }
                CommentResponse.from(it, permissionToUpdateStatus, permissionToDeleteStatus)
            }
        } else findComments?.map { CommentResponse.from(it, false, false) }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun toggleZzirit(commentId: Long, userPrincipal: UserPrincipal): ZziritStatusResponse {
        val findComment =
            commentRepository.findByIdOrNull(commentId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        var findZzirit = zziritRepository.findZziritByMemberIdAndCommentIdOrNull(userPrincipal.memberId, commentId)

        if (findZzirit == null) {
            val findSocialMember = socialMemberRepository.findByIdOrNull(userPrincipal.memberId)
                ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
            findComment.incrementZzirit()

            return ZziritStatusResponse.of(
                zziritRepository.save(
                    ZziritEntity(
                        socialMember = findSocialMember,
                        entityId = commentId,
                        zziritEntityType = ZziritEntityType.COMMENT
                    )
                )
            )
        }


        if (findZzirit.toggleZzirit()) {
            findComment.incrementZzirit()
        } else {
            findComment.decrementZzirit()
        }

        return ZziritStatusResponse.of(findZzirit)
    }

    @Transactional(readOnly = true)
    fun countZziritByCommentId(commentId: Long) = zziritRepository.countZziritByCommentId(commentId)
}
