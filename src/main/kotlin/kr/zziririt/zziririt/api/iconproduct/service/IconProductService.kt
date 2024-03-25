package kr.zziririt.zziririt.api.iconproduct.service

import jakarta.transaction.Transactional
import kr.zziririt.zziririt.api.iconproduct.dto.IconSearchCondition
import kr.zziririt.zziririt.api.iconproduct.dto.request.BuyIconProductRequest
import kr.zziririt.zziririt.api.iconproduct.dto.request.ChangeSaleStatusRequest
import kr.zziririt.zziririt.api.iconproduct.dto.request.IconProductRequest
import kr.zziririt.zziririt.api.iconproduct.dto.response.IconProductResponse
import kr.zziririt.zziririt.domain.icon.repository.IconRepository
import kr.zziririt.zziririt.domain.iconproduct.model.SaleStatus
import kr.zziririt.zziririt.domain.iconproduct.repository.IconProductRepository
import kr.zziririt.zziririt.domain.member.model.MemberIconEntity
import kr.zziririt.zziririt.domain.member.repository.MemberIconRepository
import kr.zziririt.zziririt.domain.member.repository.SocialMemberRepository
import kr.zziririt.zziririt.global.exception.ErrorCode
import kr.zziririt.zziririt.global.exception.ModelNotFoundException
import kr.zziririt.zziririt.global.exception.RestApiException
import kr.zziririt.zziririt.infra.querydsl.iconproduct.dto.IconProductRowDto
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class IconProductService(
    private val iconProductRepository: IconProductRepository,
    private val iconRepository: IconRepository,
    private val memberRepository: SocialMemberRepository,
    private val memberIconRepository: MemberIconRepository,
) {
    fun registerIconProduct(request: IconProductRequest) {
        val iconCheck =
            iconRepository.findByIdOrNull(request.iconId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        check(iconProductRepository.findByIdOrNull(request.iconId) == null) {
            throw RestApiException(ErrorCode.DUPLICATE_ICON_ID)
        }

        val iconProduct = request.toEntity(iconCheck)

        iconProductRepository.save(iconProduct)
    }

    @Transactional
    fun changeStatus(iconProductId: Long, request: ChangeSaleStatusRequest) {
        val iconProductCheck =
            iconProductRepository.findByIdOrNull(iconProductId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        iconProductCheck.changeStatus(request.saleStatus)
    }

    @Transactional
    fun deleteIconProduct(iconProductId: Long) {
        val iconProductCheck =
            iconProductRepository.findByIdOrNull(iconProductId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        iconProductRepository.delete(iconProductCheck)
    }

    @Transactional
    fun getIconProduct(iconProductId: Long): IconProductResponse {
        val iconProductCheck =
            iconProductRepository.findByIdOrNull(iconProductId) ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)

        return IconProductResponse.from(iconProductCheck)
    }

    fun getIconProducts(condition: IconSearchCondition, pageable: Pageable): PageImpl<IconProductRowDto> {

        return iconProductRepository.searchBy(
            condition,
            pageable
        )
    }

    @Transactional
    fun buyIconProduct(request: BuyIconProductRequest, userPrincipal: UserPrincipal) {
        val memberCheck = memberRepository.findByIdOrNull(userPrincipal.memberId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val iconProductCheck = iconProductRepository.findByIdOrNull(request.iconProductId)
            ?: throw ModelNotFoundException(ErrorCode.MODEL_NOT_FOUND)
        val memberIconCheck = memberIconRepository.findByIdOrNull(memberCheck.id!!)

        check(memberCheck.point > iconProductCheck.price) {
            throw RestApiException(ErrorCode.NOT_ENOUGH_POINT)
        }
        check(iconProductCheck.iconQuantity > 0) {
            throw RestApiException(ErrorCode.NOT_ENOUGH_ICONQUANTITY)
        }
        check(iconProductCheck.saleStatus == SaleStatus.SALE) {
            throw RestApiException(ErrorCode.NOT_FOR_SALE_STATUS)
        }
        check(memberIconCheck?.icon?.id != iconProductCheck.icon.id) {
            throw RestApiException(ErrorCode.ALREADY_HAVE_ICON)
        }

        iconProductCheck.reduceIconQuantityAndChangeSaleStatus()
        memberCheck.pointMinus(iconProductCheck.price)
        memberIconRepository.save(MemberIconEntity(memberCheck, iconProductCheck.icon))
    }
}