package org.services;

import org.models.MeetingRoom;

import java.util.List;
import java.util.Map;

public class RoomManagementService {
    Map<String,MeetingRoom> meetingRooms;

    public void addMeetingRoom(MeetingRoom room){
        if(!meetingRooms.containsKey(room.getId())){
            meetingRooms.put(room.getId(),room);
        }
        else throw new RuntimeException("Room already exists");
    }
    public void removeMeetingRoom(MeetingRoom room){
        meetingRooms.remove(room.getId());
    }
}
