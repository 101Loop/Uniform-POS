package com.tapatuniforms.pos.model;

public class NotificationItem {
    private String notificationTitle;
    private String notificationContent;

    public NotificationItem(String notificationTitle, String notificationContent) {
        this.notificationTitle = notificationTitle;
        this.notificationContent = notificationContent;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }
}
