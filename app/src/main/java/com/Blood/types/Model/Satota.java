package com.Blood.types.Model;

public class Satota {
private String name,number,location,token;

  public Satota(String name, String number, String location, String token) {
    this.name = name;
    this.number = number;
    this.location = location;
    this.token = token;
  }

  public Satota(String name, String number, String location) {
    this.name = name;
    this.number = number;
    this.location = location;
  }

  public Satota() {
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

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
