package com.github.codyoss.springcloudavro.config

import com.github.codyoss.springcloudavro.avro.InputMessage
import com.github.codyoss.springcloudavro.avro.OutputMessage
import com.github.codyoss.springcloudavro.converter.InputMessageConverter
import com.github.codyoss.springcloudavro.converter.OutputMessageConverter
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.stream.annotation.StreamMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {

    @Bean
    fun inputTopic(@Value("\${spring.cloud.stream.bindings.inputChannel.destination}") inputTopic: String) = inputTopic

    @Bean
    fun outputTopic(@Value("\${spring.cloud.stream.bindings.outputChannel.destination}") outputTopic: String) = outputTopic

    @Bean
    fun serdeConfig(@Value("\${app.schemaRegistryClient}") schemaRegistryClientURL: String): MutableMap<String, *> {
        return mutableMapOf(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG to schemaRegistryClientURL)
    }

    @Bean
    fun inputMessageSerde(serdeConfig: MutableMap<String, *>): SpecificAvroSerde<InputMessage> {
        val serde = SpecificAvroSerde<InputMessage>()
        serde.configure(serdeConfig, false)
        return serde
    }

    @Bean
    fun outputMessageSerde(serdeConfig: MutableMap<String, *>): SpecificAvroSerde<OutputMessage> {
        val serde = SpecificAvroSerde<OutputMessage>()
        serde.configure(serdeConfig, false)
        return serde
    }

    @Bean
    @StreamMessageConverter
    fun inputMessageConverter(inputMessageSerde: SpecificAvroSerde<InputMessage>, inputTopic: String): InputMessageConverter {
        return InputMessageConverter(inputMessageSerde, inputTopic)
    }

    @Bean
    @StreamMessageConverter
    fun outputMessageConverter(outputMessageSerde: SpecificAvroSerde<OutputMessage>, outputTopic: String): OutputMessageConverter {
        return OutputMessageConverter(outputMessageSerde, outputTopic)
    }
}
