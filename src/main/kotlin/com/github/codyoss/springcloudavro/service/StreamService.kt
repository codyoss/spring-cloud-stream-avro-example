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

    // The `@StreamListener` tells Spring to route traffic to this method for the input channel listed in the
    // annotation.
    @StreamListener(Binder.INPUT)
    // The `@Payload` tells Spring to look at the content type headers and find a message converter to deserialize the
    // bytes into the provided type.
    fun receive(@Payload msg: InputMessage) {
        val outputMessage = OutputMessage(msg.message, DateTime.now())
        // The binder can be used to send a payload to any registered channel. This is the manual way sending messages.
        // To have Spring take care of this you could instead add a `@SendTo()` annotation onto this method and provide
        // the output channel you would like data routed to. If that pattern was used on this method the signature would
        // need to be changed to return a `OutputMessage`. Spring would handle the rest.
        binder.outputChannel().send(MessageBuilder.withPayload(outputMessage).build())
    }
}
