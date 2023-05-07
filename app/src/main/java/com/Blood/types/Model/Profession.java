package com.Blood.types.Model;

public class Profession {
  private String name,number,nameProfession,token;

  public Profession (){
    // Empty cuns...
  }

  public Profession(String name, String number, String nameProfession, String token) {
    this.name = name;
    this.number = number;
    this.nameProfession = nameProfession;
    this.token = token;
  }

  public Profession(String name, String number, String nameProfession) {
    this.name = name;
    this.number = number;
    this.nameProfession = nameProfession;
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

  public String getNameProfession() {
    return nameProfession;
  }

  public void setNameProfession(String nameProfession) {
    this.nameProfession = nameProfession;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
