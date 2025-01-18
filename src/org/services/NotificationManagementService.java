package org.services;

import org.models.Notification;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class NotificationManagementService {
    private final ConcurrentHashMap<LocalDateTime, Notification> notificationMap;
    private final PriorityBlockingQueue<LocalDateTime> notificationQueue;

    public NotificationManagementService() {
        this.notificationMap = new ConcurrentHashMap<>();
        this.notificationQueue= new PriorityBlockingQueue<>();
    }
    public void pushNotificationEventInQueue(LocalDateTime notificationTime, String userId, String bookingId, String roomId) {
        Notification notification=new Notification(bookingId,"Your booking for meetingRoom" +
                roomId+"commences in 15 minutes",userId);
        notificationMap.put(notificationTime,notification);
        synchronized (this) {
            notificationQueue.add(notificationTime);
        }
        }
    public void SendNotification(){
        LocalDateTime currentTime = LocalDateTime.now();
        while (!notificationQueue.isEmpty() && !notificationQueue.peek().isAfter(currentTime)) {
            LocalDateTime notificationTime;
            synchronized (this) {
                notificationTime = notificationQueue.poll();
            }
            Notification notification = notificationMap.remove(notificationTime);
            if (notification != null) {
                deliverNotification(notification); // Simulates notification delivery
            }
        }
    }

    private void deliverNotification(Notification notification) {
        System.out.println("Sending notification to User ID: " + notification.getUserId());
        System.out.println("Message: " + notification.getMessage());
    }
}
