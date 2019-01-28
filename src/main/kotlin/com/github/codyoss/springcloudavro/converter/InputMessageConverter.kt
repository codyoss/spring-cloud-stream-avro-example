package com.github.codyoss.springcloudavro.converter

import com.github.codyoss.springcloudavro.avro.InputMessage
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.springframework.messaging.Message
import org.springframework.messaging.converter.AbstractMessageConverter
import org.springframework.util.MimeType

// This class shows the min number of methods to override to guarantee proper message deserialization. This class is not
// declared a `@Service` as it must be constructed with a factory method to have the `@StreamMessageConverter` applied.
class InputMessageConverter(private val serde: SpecificAvroSerde<InputMessage>,
                            private val inputTopic: String
): AbstractMessageConverter(MimeType.valueOf("application/*+avro")) {

    override fun supports(clazz: Class<*>): Boolean {
        return clazz == InputMessage::class.java
    }

    override fun convertFromInternal(message: Message<*>, targetClass: Class<*>, conversionHint: Any?): Any? {
        return serde.deserializer().deserialize(inputTopic, message.payload as ByteArray)
    }

    override fun canConvertFrom(message: Message<*>, targetClass: Class<*>): Boolean {
        return targetClass == InputMessage::class.java && super.supportsMimeType(message.headers)
    }
}
