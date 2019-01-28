package com.github.codyoss.springcloudavro.binder

import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.Output
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.SubscribableChannel

// This is a class Spring will find and implement. This is where the "magic" of Spring Cloud Streams comes into play.
// To properly configure channels, names of channels must match what is found in the `application.yml` file.
interface Binder {

    companion object {
        const val INPUT: String = "inputChannel"
    }

    @Input
    fun inputChannel(): SubscribableChannel

    @Output
    fun outputChannel(): MessageChannel
}
