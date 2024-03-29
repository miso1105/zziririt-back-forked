package kr.zziririt.zziririt.api.faq.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kr.zziririt.zziririt.api.faq.dto.CreateFaqRequest
import kr.zziririt.zziririt.domain.faq.model.FaqEntity
import kr.zziririt.zziririt.domain.faq.repository.FaqRepository
import org.junit.jupiter.api.extension.ExtendWith



@ExtendWith(MockKExtension::class)
class FaqServiceTest : BehaviorSpec({

    afterContainer {
        clearAllMocks()
    }

    val faqRepository = mockk<FaqRepository>()

    val faqService = FaqService(faqRepository)
    Given("Faq 생성할 때") {

        When("글을 생성하고") {
            val createFaqRequest = CreateFaqRequest("Some question", "Some answer")

            every {
                faqRepository.save(any())
            } returns FaqEntity(question = "Some question", answer = "Some answer").apply { this.id= 1L }

            Then("저장이 되어야한다.") {
                val response = faqService.createFaq(createFaqRequest)
                response shouldNotBe null
                response.question shouldBe "Some question"
                response.answer shouldBe "Some answer"

                verify {
                    faqRepository.save(any())
                }
            }
        }
    }
})
