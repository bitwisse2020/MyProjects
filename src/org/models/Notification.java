package org.models;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Notification {
    private String bookingId;
    private String message;
    private String userId;

    public Notification(String bookingId, String message, String userId) {
        this.bookingId = bookingId;
        this.message = message;
        this.userId = userId;
    }
}
