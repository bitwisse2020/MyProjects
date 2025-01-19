package org.services;

import org.models.Booking;


import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BookingManagementService {
    private final RoomManagementService roomManagementService;
    private final NotificationManagementService notificationManagementService;
    private ConcurrentHashMap<String,List<Booking>> bookings= new ConcurrentHashMap<>();

    public BookingManagementService(RoomManagementService roomManagementService, NotificationManagementService notificationManagementService) {
        this.roomManagementService = roomManagementService;
        this.notificationManagementService = notificationManagementService;
    }

    public void addMeetingRoom(String roomId){
        this.roomManagementService.addMeetingRoom(roomId);
    }
    public void removeMeetingRoom(String roomId){
        synchronized (roomId.intern()) {
            List<Booking> bookingList=this.bookings.get(roomId);
            if(isRoomWithFutureBookings(roomId, bookingList)){
                throw new RuntimeException("This Room has bookings!! Can't be removed");
            }
            this.roomManagementService.removeMeetingRoom(roomId);
            this.bookings.remove(roomId);
        }
    }

    private static boolean isRoomWithFutureBookings(String roomId, List<Booking> bookingList) {
        return bookingList.stream().anyMatch(booking -> (Objects.equals(booking.getMeetingRoomId(), roomId) &&
                booking.getEndTime().isAfter(LocalDateTime.now())));
    }

    public void bookMeetingRoom(String roomId, String userId, LocalDateTime startTime, LocalDateTime endTime,int userCount){
        if(!roomManagementService.meetingRooms.containsKey(roomId)){
            throw new RuntimeException("invalid room!!");
        }
        if(startTime.isBefore(LocalDateTime.now())){
            throw new RuntimeException("can't book for past time");
        }
        if(endTime.isBefore(startTime)){
            throw new RuntimeException("invalid interval!!");
        }
        if(roomManagementService.meetingRooms.get(roomId).getCapacity()<userCount){
            throw new RuntimeException("required capacity exceeds selected room capacity, choose another room");
        }
        synchronized (roomId.intern()){
            List<Booking> bookingList=this.bookings.get(roomId);
            List<Booking> allBookingsForRoom = bookingList.stream().
                filter(booking -> booking.getMeetingRoomId().equals(roomId)).
                filter(booking ->
                        !(booking.getEndTime().equals(startTime) || booking.getStartTime().equals(endTime)) &&
                                (booking.getStartTime().isBefore(endTime) && booking.getEndTime().isAfter(startTime))).
                toList();

            if (allBookingsForRoom.isEmpty()){
                throw new RuntimeException("Room already booked for the provided time interval");
            }
            String bookingId = UUID.randomUUID().toString();
            Booking newBooking = new Booking(bookingId, roomId, startTime, endTime, userId);
            bookingList.add(newBooking);
            bookings.put(roomId,bookingList);
            notificationManagementService.pushNotificationEventInQueue(startTime.minus(15, ChronoUnit.MINUTES), userId, bookingId, roomId);
        }
    }
    public void removeBooking(String bookingId){

    }



}
