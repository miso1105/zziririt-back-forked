package kr.zziririt.zziririt.api.iconproduct.controller

import kr.zziririt.zziririt.api.iconproduct.dto.IconSearchCondition
import kr.zziririt.zziririt.api.iconproduct.dto.request.BuyIconProductRequest
import kr.zziririt.zziririt.api.iconproduct.dto.request.ChangeSaleStatusRequest
import kr.zziririt.zziririt.api.iconproduct.dto.request.IconProductRequest
import kr.zziririt.zziririt.api.iconproduct.service.IconProductService
import kr.zziririt.zziririt.global.responseEntity
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/iconproduct")
class IconProductController(
    private val iconProductService: IconProductService,
) {

    @PostMapping("/registration")
    fun registerIconProduct(
        @RequestBody request: IconProductRequest
    ) = responseEntity(HttpStatus.OK) { iconProductService.registerIconProduct(request) }


    @PutMapping("/{iconProductId}")
    fun changeSaleStatus(
        @PathVariable iconProductId: Long,
        @RequestBody request: ChangeSaleStatusRequest
    ) = responseEntity(HttpStatus.OK) { iconProductService.changeStatus(iconProductId, request) }

    @DeleteMapping("/{iconProductId}")
    fun deleteIconProduct(
        @PathVariable iconProductId: Long,
    ) = responseEntity(HttpStatus.OK) { iconProductService.deleteIconProduct(iconProductId) }

    @GetMapping("/{iconProductId}")
    fun getIconProduct(
        @PathVariable iconProductId: Long
    ) = responseEntity(HttpStatus.OK) { iconProductService.getIconProduct(iconProductId) }

    @GetMapping()
    fun getIconProducts(
        condition: IconSearchCondition,
        @PageableDefault(
            size = 30,
            sort = ["createdAt"]
        ) pageable: Pageable
    ) = responseEntity(HttpStatus.OK) { iconProductService.getIconProducts(condition, pageable)}

    @PostMapping("/purchase")
    fun buyIconProduct(
        @RequestBody request: BuyIconProductRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ) = responseEntity(HttpStatus.OK) { iconProductService.buyIconProduct(request, userPrincipal)}
}