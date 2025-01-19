package org.models;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingRoom {
    private String name;
    private String id;
    private int capacity;
}
