package kr.zziririt.zziririt.api.icon.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kr.zziririt.zziririt.api.icon.dto.request.AddIconRequest
import kr.zziririt.zziririt.api.icon.dto.request.GiveIconToMemberRequest
import kr.zziririt.zziririt.api.icon.service.IconService
import kr.zziririt.zziririt.global.responseEntity
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/icons")
class IconController(
    private val iconService: IconService
) {
    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    fun addIcon(
        @RequestParam request: String,
        @RequestParam file: List<MultipartFile>,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ) = responseEntity(HttpStatus.OK) {
        val mapper = jacksonObjectMapper()
        val requestObj = mapper.readValue(request, AddIconRequest::class.java)
        iconService.addIcon(requestObj, file, userPrincipal) }

    @DeleteMapping("/{iconId}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteIcon(
        @PathVariable iconId: Long,
    ) = responseEntity(HttpStatus.OK) { iconService.deleteIcon(iconId) }

    @GetMapping("/myicons")
    fun getMyIcons(
        @PageableDefault(
            size = 10,
            sort = ["createdAt"]
        ) pageable: Pageable,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ) = responseEntity(HttpStatus.OK) { iconService.getMyIcons(pageable, userPrincipal) }

    @PostMapping("/payment")
    @PreAuthorize("hasRole('ADMIN')")
    fun giveIconToMember(
        @RequestBody request: GiveIconToMemberRequest
    ) = responseEntity(HttpStatus.OK) { iconService.giveIconToMember(request) }
}