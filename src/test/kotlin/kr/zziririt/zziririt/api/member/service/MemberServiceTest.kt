package kr.zziririt.zziririt.api.member.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.Test
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.zziririt.zziririt.api.member.controller.MemberController
import kr.zziririt.zziririt.api.member.dto.request.AdjustRoleRequest
import kr.zziririt.zziririt.api.member.dto.response.GetMemberResponse
import kr.zziririt.zziririt.domain.member.model.*
import kr.zziririt.zziririt.domain.member.repository.LoginHistoryRepository
import kr.zziririt.zziririt.domain.member.repository.MemberIconRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.global.exception.RestApiException
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.time.LocalDateTime

@Test
@WebMvcTest(controllers = [MemberController::class])
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
class MemberServiceTest() : BehaviorSpec({

    @BeforeEach
    fun setUp() {
        val userPrincipal = UserPrincipal(1L, "email", roles = setOf(MemberRole.ADMIN.name), "providerId")
        val authentication = UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }


    val memberRepository = mockk<SocialMemberRepository>()
    val memberIconRepository = mockk<MemberIconRepository>()
    val loginHistoryRepository = mockk<LoginHistoryRepository>()
    val memberService = MemberService(memberRepository, memberIconRepository, loginHistoryRepository)

    val userPrincipal = UserPrincipal(1L, "email", roles = setOf(MemberRole.ADMIN.name), "providerId")


    Given("관리자가 회원의 정보를 바꾸려고 할 때") {
        val memberId = 1L
        val request = AdjustRoleRequest(MemberRole.ADMIN)

        val normalMemberId = 2L

        When("adjustRole 매서드를 사용해서") {
            val viewerMember = SocialMemberEntity(
                "email",
                "nickname",
                OAuth2Provider.TEST,
                "providerId",
                MemberRole.VIEWER,
                MemberStatus.NORMAL
            )
            every { memberRepository.findByIdOrNull(normalMemberId) } returns viewerMember

            memberService.adjustRole(normalMemberId, request, userPrincipal)

            Then("유저 정보를 변경할 할 수 있다. 여기서는 Admin으로 변경함") {
                viewerMember.memberRole shouldBe MemberRole.ADMIN
                //멤버 등급이 변경되지 않으면 안된다.
                viewerMember.memberRole shouldNotBe MemberRole.VIEWER
                //ADMIN이 아니라 BOARD_MANAGER로 변경되면 안된다.
                viewerMember.memberRole shouldNotBe MemberRole.BOARD_MANAGER
                //ADMIN이 아니라 STREAMER로 변경되면 안된다.
                viewerMember.memberRole shouldNotBe MemberRole.STREAMER
            }
        }
    }

    Given("관리자가 회원의 정보를 바꾸려고 할 때2") {
        val request = AdjustRoleRequest(MemberRole.ADMIN)

        val normalMemberId = 2L

        When("지정한 유저가 없는 유저라면") {
            every { memberRepository.findByIdOrNull(normalMemberId) } returns null

            Then("ModelNotFoundException 발생해야 한다.") {
                shouldThrow<ModelNotFoundException> { memberService.adjustRole(normalMemberId, request, userPrincipal) }
            }
        }
    }

    Given("관리자가 회원의 정보를 바꾸려고 할 때3") {
        val request = AdjustRoleRequest(MemberRole.VIEWER)

        val normalMemberId = 2L

        When("회원의 정보와 바꾸려는 정보가 이미 같다면") {
            val viewerMember = SocialMemberEntity(
                "email",
                "nickname",
                OAuth2Provider.TEST,
                "providerId",
                MemberRole.VIEWER,
                MemberStatus.NORMAL
            )
            every { memberRepository.findByIdOrNull(normalMemberId) } returns viewerMember

            Then("ModelNotFoundException 발생해야 한다.") {
                shouldThrow<RestApiException> { memberService.adjustRole(normalMemberId, request, userPrincipal) }
            }
        }
    }

    Given("유저가 내 정보 조회를 할 때") {
        When("내 정보와 토큰의 정보가 일치한다면") {
            val testMember = SocialMemberEntity(
                "email",
                "nickname",
                OAuth2Provider.TEST,
                "providerId",
                MemberRole.VIEWER,
                MemberStatus.NORMAL
            )
            every { memberRepository.findByIdOrNull(userPrincipal.memberId) } returns testMember
            val loginHistoryEntity =
                LoginHistoryEntity(memberId = testMember, ip = "ip", userEnvironment = "environment")
            loginHistoryEntity.createdAt = LocalDateTime.now()
            every { loginHistoryRepository.findTopByMemberIdOrderByCreatedAtDesc(testMember) } returns loginHistoryEntity

            Then("유저 정보가 반환되어야 한다.") {
                GetMemberResponse(
                    nickname = testMember.nickname,
                    memberStatus = testMember.memberStatus,
                    lastLogin = loginHistoryEntity.createdAt
                )
            }
        }
    }

    Given("유저가 내 정보 조회를 할 때2") {
        When("내 정보와 토큰의 정보가 일치하지 않다면") {
            every { memberRepository.findByIdOrNull(userPrincipal.memberId) } returns null

            Then("ModelNotFoundException이 발생해야 한다.") {
                shouldThrow<ModelNotFoundException> { memberService.getMember(userPrincipal) }
            }
        }
    }
})