package org.models;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class Notification {
    public String bookingId;
    public String message;
    public String userId;

    public Notification(String bookingId, String message, String userId) {
        this.bookingId = bookingId;
        this.message = message;
        this.userId = userId;
    }
}
