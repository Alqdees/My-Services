package com.Blood.types.Model;

public class Model {
    private String ID, Name ,number,type,location;

    // Empty Constructor
    public Model(){

    }

    public Model(String name, String number, String type, String location) {
        Name = name;
        this.number = number;
        this.type = type;
        this.location = location;
    }

    public Model(String ID, String name, String number, String type, String location) {
        this.ID = ID;
        Name = name;
        this.number = number;
        this.type = type;
        this.location = location;
    }

    public String getName() {
        return Name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
