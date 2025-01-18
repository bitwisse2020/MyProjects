package org.services;

import org.models.Booking;


import java.util.*;

public class BookingManagementService {
    private  long fifteenMinutes=15*60*1000;
    private RoomManagementService roomManagementService;
    private NotificationManagementService notificationManagementService;
    Map<String, Booking> bookingMap= new HashMap<>();

    public void bookMeetingRoom(String roomId, String userId, Date startTime,Date endTime){
        if(!roomManagementService.meetingRooms.containsKey(roomId)){
            throw new RuntimeException("invalid room!!");
        }
        if(startTime.before(new Date())){
            throw new RuntimeException("can't book for past time");
        }
        if(endTime.before(startTime)){
            throw new RuntimeException("invalid interval!!");
        }
        List<Booking> allBookingsForRoom = bookingMap.values().stream().
                filter(booking -> booking.meetingRoomId.equals(roomId)).
                filter(booking -> (booking.startTime.before(endTime)&& booking.endTime.after(endTime))
                ||(booking.startTime.after(startTime)&& booking.endTime.before(endTime))
                || (booking.startTime.equals(startTime)&& booking.endTime.equals(endTime))).
                toList();

        if (allBookingsForRoom.size()>0){
            throw new RuntimeException("invalid time interval!! causing booking overlap");
        }
        else {
            String bookingId=UUID.randomUUID().toString();
            Booking newBooking= new Booking(bookingId,roomId,startTime,endTime,userId);
            bookingMap.put(bookingId,newBooking);
            notificationManagementService.pushNotificationEventInQueue(new Date(startTime.getTime()-fifteenMinutes),userId,bookingId,roomId);
        }
    }
    public void removeBooking(String bookingId){
        bookingMap.remove(bookingId);
    }



}
