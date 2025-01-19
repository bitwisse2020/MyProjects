package org.services;

import org.models.MeetingRoom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManagementService {
    final ConcurrentHashMap<String,MeetingRoom> meetingRooms;

    public RoomManagementService() {
        this.meetingRooms = new ConcurrentHashMap<>();
    }

    public void addMeetingRoom(String roomId){
        if(!meetingRooms.containsKey(roomId)){
            meetingRooms.put(roomId,MeetingRoom.builder().id(roomId).build());
        }
        else throw new RuntimeException("Room already exists");
    }
    public void removeMeetingRoom(String roomId){
        meetingRooms.remove(roomId);
    }
}
