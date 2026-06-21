package com.example.hotel_reservation_system.dto;

public class GuestDTO {
    private String fullName;
    private String email;
    private String phone;
    private String nationalId;
    private String password;

    public String getFullName()          { return fullName; }
    public void setFullName(String n)    { this.fullName = n; }
    public String getEmail()             { return email; }
    public void setEmail(String e)       { this.email = e; }
    public String getPhone()             { return phone; }
    public void setPhone(String p)       { this.phone = p; }
    public String getNationalId()        { return nationalId; }
    public void setNationalId(String n)  { this.nationalId = n; }
    public String getPassword()          { return password; }
    public void setPassword(String p)    { this.password = p; }
}