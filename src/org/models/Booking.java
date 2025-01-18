package org.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class Booking {
    private String bookingId;
    private String meetingRoomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String userId;

    public Booking(String toString, String roomId, LocalDateTime startTime, LocalDateTime endTime, String userId) {
    }
}
