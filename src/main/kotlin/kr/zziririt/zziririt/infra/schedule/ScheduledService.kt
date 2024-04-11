//package kr.zziririt.zziririt.infra.schedule
//
//import io.github.oshai.kotlinlogging.KotlinLogging
//import net.javacrumbs.shedlock.core.LockAssert
//import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
//import org.springframework.scheduling.annotation.Scheduled
//import org.springframework.stereotype.Service
//
//@Service
//class ScheduledService {
//    private val logger = KotlinLogging.logger {}
//
//    @Scheduled(fixedRate = 1000) // 스케줄러가 돌아가고 1초 뒤에 락이 걸리도록
//    @SchedulerLock(name = "boardStatusActiveToInactive", lockAtLeastFor = "14m", lockAtMostFor = "14m")
//    fun boardStatusActiveToInactive() {
//        // To assert that the lock is held (prevents misconfiguration errors)
//        LockAssert.assertLocked()
//        // do something
//        logger.debug { "scheduled task" }
//    }
//}