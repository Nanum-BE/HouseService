package com.nanum.houseservice.kafka.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nanum.houseservice.kafka.dto.KafkaRoomDto;
import com.nanum.houseservice.room.domain.Room;
import com.nanum.houseservice.room.infrastructure.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerImpl implements KafkaConsumer {
    private final RoomRepository roomRepository;

    @Override
    public void updateRoomStatus(String kafkaMessage) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        KafkaRoomDto kafkaRoomDto = objectMapper.registerModule(new JavaTimeModule()).readValue(kafkaMessage, KafkaRoomDto.class);

        if (kafkaRoomDto.getMessage().equals("contract")) {
            roomRepository.replaceStatusToProgress(kafkaRoomDto.getRoomId());
        } else if (kafkaRoomDto.getMessage().equals("completed")) {
            roomRepository.replaceStatusToCOMPLETION(kafkaRoomDto.getRoomId());
            roomRepository.replaceDate( kafkaRoomDto.getEndDate(), kafkaRoomDto.getRoomId());
        }

    }
}
