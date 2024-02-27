package kr.zziririt.zziririt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ZziriritApplication

fun main(args: Array<String>) {
    runApplication<ZziriritApplication>(*args)
}