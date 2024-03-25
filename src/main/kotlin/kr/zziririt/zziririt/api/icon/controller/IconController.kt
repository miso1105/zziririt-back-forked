package kr.zziririt.zziririt.api.icon.controller

import kr.zziririt.zziririt.api.icon.dto.request.AddIconRequest
import kr.zziririt.zziririt.api.icon.service.IconService
import kr.zziririt.zziririt.global.responseEntity
import kr.zziririt.zziririt.infra.security.UserPrincipal
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/icons")
class IconController(
    private val iconService: IconService
) {
    @PostMapping("/add")
    fun addIcon(
        @RequestPart request: AddIconRequest,
        @RequestPart file: List<MultipartFile>,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ) = responseEntity(HttpStatus.OK) { iconService.addIcon(request, file, userPrincipal) }

    @DeleteMapping("/{iconId}")
    fun deleteIcon(
        @PathVariable iconId: Long,
    ) = responseEntity(HttpStatus.OK) { iconService.deleteIcon(iconId) }

    @GetMapping("/myicon")
    fun getMyIcons(
        @PageableDefault(
            size = 10,
            sort = ["createdAt"]
        ) pageable: Pageable,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ) = responseEntity(HttpStatus.OK) { iconService.getMyIcons(pageable, userPrincipal) }

}