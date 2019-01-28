package com.github.codyoss.springcloudavro.binder

import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.Output
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.SubscribableChannel

interface Binder {

    companion object {
        const val INPUT: String = "inputChannel"
    }

    @Input
    fun inputChannel(): SubscribableChannel

    @Output
    fun outputChannel(): MessageChannel
}
