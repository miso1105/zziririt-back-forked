package kr.zziririt.zziririt.domain.post.model

import io.kotest.core.spec.style.FeatureSpec
import io.kotest.matchers.shouldBe
import kr.zziririt.zziririt.domain.board.model.BoardEntity
import kr.zziririt.zziririt.domain.board.model.BoardType
import kr.zziririt.zziririt.domain.member.model.MemberRole
import kr.zziririt.zziririt.domain.member.model.MemberStatus
import kr.zziririt.zziririt.domain.member.model.OAuth2Provider
import kr.zziririt.zziririt.domain.member.model.SocialMemberEntity

class PostEntityTest : FeatureSpec({
    val socialMember = SocialMemberEntity("email", "nickname", OAuth2Provider.TEST, "providerId", MemberRole.VIEWER, MemberStatus.NORMAL)
    val board = BoardEntity(socialMember = socialMember, boardName = "자유 게시판", boardUrl = "board Url", boardType = BoardType.ZZIRIRIT_BOARD)
    val postFixture = PostEntity(board, socialMember, "title", "content")

    feature("Post Entity update 메서드 정상 동작 검증") {
        scenario("update 메서드를 사용하여 title과 content를 변경시 정상적으로 변경되어야 한다.") {
            postFixture.update("updated Title", "updated Content")
        }
    }
    feature("Post Entity incrementZzirit 메서드 정상 동작 검증") {
        scenario("incrementZzirit 메서드를 사용할 경우 Post Entity의 ZziritCount가 1증가하여야 한다.") {
            val beforeZziritCount = postFixture.zziritCount

            postFixture.incrementZzirit()

            postFixture.zziritCount shouldBe beforeZziritCount + 1
        }
    }
    feature("Post Entity incrementHit 메서드 정상 동작 검증") {
        scenario("incrementHit 메서드를 사용할 경우 Post Entity의 hit이 1증가하여야 한다.") {
            val beforeHit = postFixture.hit

            postFixture.incrementHit()

            postFixture.hit shouldBe beforeHit + 1
        }
    }
    feature("Post Entity togglePrivateStatus 메서드 정상 동작 검증") {
        scenario("togglePrivateStatus 메서드를 사용할 경우 Post Entity의 privateStatus가 변경되어야 한다.") {
            val beforePrivateStatus = postFixture.privateStatus

            postFixture.togglePrivateStatus()

            postFixture.privateStatus shouldBe !beforePrivateStatus
        }
    }
    feature("Post Entity toggleBlindStatus 메서드 정상 동작 검증") {
        scenario("toggleBlindStatus 메서드를 사용할 경우 Post Entity의 blindStatus가 변경되어야 한다.") {
            val beforeBlindStatus = postFixture.blindStatus

            postFixture.toggleBlindStatus()

            postFixture.blindStatus shouldBe !beforeBlindStatus
        }
    }
})