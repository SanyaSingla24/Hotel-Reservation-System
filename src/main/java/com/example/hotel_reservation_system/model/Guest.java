package com.example.hotel_reservation_system.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
@Entity
@Table(name = "guests")
public class Guest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String nationalId;
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "guest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public Guest() {}

    public Long getId()                              { return id; }
    public String getFullName()                      { return fullName; }
    public void setFullName(String n)                { this.fullName = n; }
    public String getEmail()                         { return email; }
    public void setEmail(String e)                   { this.email = e; }
    public String getPhone()                         { return phone; }
    public void setPhone(String p)                   { this.phone = p; }
    public String getNationalId()                    { return nationalId; }
    public void setNationalId(String n)              { this.nationalId = n; }
    public String getPassword()                      { return password; }
    public void setPassword(String p)                { this.password = p; }
    public List<Reservation> getReservations()       { return reservations; }
    public void setReservations(List<Reservation> r) { this.reservations = r; }
}