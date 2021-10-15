package com.example.firebase_app_chat.Models;

import java.util.Date;

public class chatMessage {
    String senderId,receiverId,message,dateTime;
    Date date;
    String conversionId,conversionName,conversionImg;

    public chatMessage() {
    }

    public chatMessage(String senderId, String receiverId, String message, String dateTime, Date date, String conversionId, String conversionName, String conversionImg) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.dateTime = dateTime;
        this.date = date;
        this.conversionId = conversionId;
        this.conversionName = conversionName;
        this.conversionImg = conversionImg;
    }

    public String getConversionId() {
        return conversionId;
    }

    public void setConversionId(String conversionId) {
        this.conversionId = conversionId;
    }

    public String getConversionName() {
        return conversionName;
    }

    public void setConversionName(String conversionName) {
        this.conversionName = conversionName;
    }

    public String getConversionImg() {
        return conversionImg;
    }

    public void setConversionImg(String conversionImg) {
        this.conversionImg = conversionImg;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
