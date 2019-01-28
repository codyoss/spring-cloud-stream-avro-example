package com.github.codyoss.springcloudavro.service

import com.github.codyoss.springcloudavro.avro.InputMessage
import com.github.codyoss.springcloudavro.avro.OutputMessage
import com.github.codyoss.springcloudavro.binder.Binder
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.hamcrest.Matchers
import org.joda.time.DateTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.test.binder.MessageCollector
import org.springframework.integration.support.MessageBuilder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ActiveProfiles("integration")
@SpringBootTest
class StreamServiceTest {
    @Autowired
    private lateinit var binder: Binder
    @Autowired
    private lateinit var messageCollector: MessageCollector
    @Autowired
    private lateinit var inputMessageSerde: SpecificAvroSerde<InputMessage>
    @Autowired
    private lateinit var outputMessageSerde: SpecificAvroSerde<OutputMessage>

    @Test
    fun integrationTest() {
        val msg = "Hello there"
        val now = DateTime.now().millis
        binder.inputChannel()
            .send(
                MessageBuilder.withPayload(
                    inputMessageSerde.serializer().serialize("input-topic", InputMessage(msg)))
                    .build()
            )
        val output = messageCollector.forChannel(binder.outputChannel()).poll()
        val outputMessage = outputMessageSerde.deserializer().deserialize("output-topic", output.payload as ByteArray)

        assertEquals(msg, outputMessage.message)
        assertThat(outputMessage.timestamp.millis, Matchers.allOf(Matchers.greaterThan(now),Matchers.lessThan(DateTime.now().millis)))
    }
}
