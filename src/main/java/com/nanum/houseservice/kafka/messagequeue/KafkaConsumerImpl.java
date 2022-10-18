package com.nanum.houseservice.kafka.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nanum.config.RoomStatus;
import com.nanum.houseservice.house.domain.House;
import com.nanum.houseservice.kafka.dto.KafkaRoomDto;
import com.nanum.houseservice.room.domain.Room;
import com.nanum.houseservice.room.infrastructure.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerImpl implements KafkaConsumer {
    private final RoomRepository roomRepository;

    @Override
    public void updateRoomStatus(String kafkaMessage) throws JsonProcessingException {
        log.info("Kafka Message: ->" + kafkaMessage);

        ObjectMapper objectMapper = new ObjectMapper();
        KafkaRoomDto kafkaRoomDto = objectMapper.readValue(kafkaMessage, KafkaRoomDto.class);

        if (kafkaRoomDto.getMessage().equals("contract")) {
            roomRepository.replaceStatusToProgress(kafkaRoomDto.getRoomId());
        } else if (kafkaRoomDto.getMessage().equals("completed")) {
            roomRepository.replaceStatusToCOMPLETION(kafkaRoomDto.getRoomId());
        }

    }

//    @Override
//    public void updateRoomStatusToComplete(String kafkaMessage) {
//
//        Map<Object, Object> map = new HashMap<>();
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {
//            });
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//
//        Optional<Room> room = roomRepository.findById((Long) map.get("roomId"));
//
//        room.ifPresent(value -> roomRepository.replaceStatusToCOMPLETION(value.getId()));
//
//    }
}
