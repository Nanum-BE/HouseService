package com.nanum.houseservice.kafka.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;

public interface KafkaConsumer {
    @KafkaListener(topics = "house-topic")
    void updateRoomStatus(String kafkaMessage) throws JsonProcessingException;
}
