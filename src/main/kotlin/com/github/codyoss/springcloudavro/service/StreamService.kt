package com.github.codyoss.springcloudavro.service

import com.github.codyoss.springcloudavro.avro.InputMessage
import com.github.codyoss.springcloudavro.avro.OutputMessage
import com.github.codyoss.springcloudavro.binder.Binder
import org.joda.time.DateTime
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class StreamService(private val binder: Binder) {

    @StreamListener(Binder.INPUT)
    fun receive(@Payload msg: InputMessage) {
        val outputMessage = OutputMessage(msg.message, DateTime.now())
        binder.outputChannel().send(MessageBuilder.withPayload(outputMessage).build())
    }
}
