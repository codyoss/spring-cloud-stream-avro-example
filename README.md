# Spring Cloud Stream /w Avro(Confluent) serialized Kafka messages

## What 

This project it meant to act as a reference on how to get these technologies to work together. It also shows how to 
write a simple integration test to prove your project is working as expected.

## Build

`./gradlew clean build` 

Note I have pre-generated the avro objects and bundled them into the project by running `./gradlew generateAvro`.

## Test 

Create zookeeper, kafka brokers, schema registry, and the topics:
`sudo docker-compose up -d`

Run the integration tests using the docker services stood up in the step above:
 `./gradlew integrationTest`

## Why

When I was working on a project at work a few weeks back I was tasked with making these things play nice together.
At first I thought easy enough, I will just include the the `spring-cloud-starter-stream-kafka` project, set the
content types to `application/*+avro` and the magic of the Spring Cloud Stream framework should abstract away the tricky
bits and everything will just work! Wrong!

Well I should say this was wrong for my teams use case. You see the issue was the avro app that was feeding this
app used Confluent's `kafka-streams` technology. Confluent and Spring take different approaches on how they handle
versioning of the avro schemas. Confluent adds some magic bits to the start of the message to tell the consumer
what version to pull from the schema registry. While Spring on the other hand makes use of content type headers that
include a suffix stating the version. This means if you have Spring try to read a Confluent avro message it will
be super confused even if you override the content type. This is because it will choke on trying to read those
magic version bits at the front of the message.

So to solve this problem I really had three options if I wanted to stick with using Spring Cloud Streams.

1. Have the producing app switch to the Spring way of versioning and serializing messages. This option was not desirable
as other applications were already reading these kafka messages. So... thank you, next.

2. Write a custom serializer/deserializer for the Spring Cloud Stream app where I throw away the magic bits and then
try to serialize/deserialize the message using the avro message converter provided by Spring. This seemed a bit hacky
to me.

3. Wrap the same serializer/deserializer that Confluent uses up in a custom message converter. This seemed like both
the least amount of work and the least hacky way of making the technologies play nice with one another.

Looking back knowing what I know now I think I would have not used Spring Cloud Stream for my use case. Having to
write my own message converters kinda made me feel like the "magic" this framework was providing me was lost. But,
in case anyone else ever has this use case and wants these things to work together, this is how to do it.

I mainly made this repo to raise awareness of the compatibility issues I faced. There was no good reference online
telling me this info so I hope this can help at least one other person same some time down the road.
 
## Notes

- I use Ubuntu, so the above commands reflect this
- It may come across as I don't like Spring Cloud Streams from reading above. And although this is mostly true
it has nothing to do with all of these avro issues I was facing. The framework is just a little too magical for me.
But I can see how it can be nice for getting a pipeline up and running quickly; it is just not for me.
- Have a good day.
