package org.services;

import org.models.Booking;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BookingManagementService {
    private final RoomManagementService roomManagementService;
    private final NotificationManagementService notificationManagementService;
    private final ConcurrentHashMap<String, Booking> bookingMap= new ConcurrentHashMap<>();

    public BookingManagementService(RoomManagementService roomManagementService, NotificationManagementService notificationManagementService) {
        this.roomManagementService = roomManagementService;
        this.notificationManagementService = notificationManagementService;
    }

    public void bookMeetingRoom(String roomId, String userId, LocalDateTime startTime, LocalDateTime endTime){
        if(!roomManagementService.meetingRooms.containsKey(roomId)){
            throw new RuntimeException("invalid room!!");
        }
        if(startTime.isBefore(LocalDateTime.now())){
            throw new RuntimeException("can't book for past time");
        }
        if(endTime.isBefore(startTime)){
            throw new RuntimeException("invalid interval!!");
        }
        synchronized (roomId.intern()){
        List<Booking> allBookingsForRoom = bookingMap.values().stream().
                filter(booking -> booking.getMeetingRoomId().equals(roomId)).
                filter(booking ->
                        !(booking.getEndTime().equals(startTime) || booking.getStartTime().equals(endTime)) &&
                                (booking.getStartTime().isBefore(endTime) && booking.getEndTime().isAfter(startTime))).
                toList();

        if (allBookingsForRoom.isEmpty()){
            throw new RuntimeException("invalid time interval!! causing booking overlap");
        }
        String bookingId = UUID.randomUUID().toString();
        Booking newBooking = new Booking(bookingId, roomId, startTime, endTime, userId);
        bookingMap.put(bookingId, newBooking);
        notificationManagementService.pushNotificationEventInQueue(startTime.minus(15, ChronoUnit.MINUTES), userId, bookingId, roomId);
        }
    }
    public void removeBooking(String bookingId){
        bookingMap.remove(bookingId);
    }



}
