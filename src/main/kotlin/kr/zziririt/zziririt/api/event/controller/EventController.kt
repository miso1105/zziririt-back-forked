//package kr.zziririt.zziririt.api.event.controller
//
//import kr.zziririt.zziririt.api.event.dto.EventRegisterRequest
//import kr.zziririt.zziririt.api.event.service.EventService
//import kr.zziririt.zziririt.global.responseEntity
//import org.apache.coyote.Response
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.PostMapping
//import org.springframework.web.bind.annotation.RequestBody
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/api/v1/events")
//class EventController(
//    private val eventService: EventService
//) {
//    @PostMapping
//    fun registerEvent(
//        @RequestBody request: EventRegisterRequest
//    ): ResponseEntity<Long> {
//        eventService.registerEvent(request).let { responseEntity(HttpStatus.CREATED).body(it) }
//    }
//}