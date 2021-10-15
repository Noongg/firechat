package com.example.firebase_app_chat.Models;

public class UserDb {
    String avt,yourname,birthday, phone, gender, cover,story,password,id;

    public UserDb() {

    }

    public UserDb(String avt, String yourname, String birthday, String phone, String gender, String cover, String story, String password, String id) {
        this.avt = avt;
        this.yourname = yourname;
        this.birthday = birthday;
        this.phone = phone;
        this.gender = gender;
        this.cover = cover;
        this.story = story;
        this.password = password;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvt() {
        return avt;
    }

    public void setAvt(String avt) {
        this.avt = avt;
    }

    public String getYourname() {
        return yourname;
    }

    public void setYourname(String yourname) {
        this.yourname = yourname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
