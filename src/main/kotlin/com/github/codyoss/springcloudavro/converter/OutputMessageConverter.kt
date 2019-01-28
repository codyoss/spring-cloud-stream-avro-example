package com.github.codyoss.springcloudavro.converter

import com.github.codyoss.springcloudavro.avro.OutputMessage
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.converter.AbstractMessageConverter
import org.springframework.util.MimeType

// This class shows the min number of methods to override to guarantee proper message serialization. This class is not
// declared a `@Service` as it must be constructed with a factory method to have the `@StreamMessageConverter` applied.
class OutputMessageConverter(private val serde: SpecificAvroSerde<OutputMessage>,
                             private val outputTopic: String
): AbstractMessageConverter(MimeType.valueOf("application/*+avro")) {
    override fun supports(clazz: Class<*>): Boolean {
        return clazz == OutputMessage::class.java
    }

    override fun convertToInternal(payload: Any, headers: MessageHeaders?, conversionHint: Any?): Any? {
        return serde.serializer().serialize(outputTopic, payload as OutputMessage)
    }
}
