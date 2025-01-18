package org.services;

import org.models.Notification;

import java.util.*;

public class NotificationManagementService {
    private Map<Date, Notification> notificationMap;
    private Queue<Date> notificationQueue;

    public NotificationManagementService() {
        this.notificationMap = new HashMap<>();
        this.notificationQueue= new PriorityQueue<>(Comparator.naturalOrder());
    }
    public void pushNotificationEventInQueue(Date notificationTime, String userId,String bookingId,String roomId) {
        Notification notification=new Notification(bookingId,"Your booking for meetingRoom" +
                roomId+"commences in 15 minutes",userId);
        notificationMap.put(notificationTime,notification);
        notificationQueue.add(notificationTime);
    }
    public void SendNotification(){
        Date currentTime= new Date();
        if(notificationQueue.poll().equals(currentTime)){
            Notification notification=notificationMap.remove(currentTime);

        }
    }
}
