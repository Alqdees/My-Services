package com.Blood.types.Model;

public class Doctor {
    private String name,number,title,specialization,presence;


    public Doctor(String name, String number, String title, String specialization, String presence) {
        this.name = name;
        this.number = number;
        this.title = title;
        this.specialization = specialization;
        this.presence = presence;
    }

    public Doctor (){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPresence() {
        return presence;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }
}
