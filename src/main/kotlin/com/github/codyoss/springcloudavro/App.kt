package com.github.codyoss.springcloudavro

import com.github.codyoss.springcloudavro.binder.Binder
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding

@SpringBootApplication
@EnableBinding(Binder::class)
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
