package com.nanum.houseservice.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaRoomDto {
    private Long roomId;
    private String message;
    private LocalDate endDate;
}
