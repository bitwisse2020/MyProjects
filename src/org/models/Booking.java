package org.models;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Booking {
    public String bookingId;
    public String meetingRoomId;
    public Date startTime;
    public Date endTime;
    public String userId;

    public Booking(String toString, String roomId, Date startTime, Date endTime, String userId) {
    }
}
