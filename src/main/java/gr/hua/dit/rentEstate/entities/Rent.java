package gr.hua.dit.rentEstate.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rent")
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String identity;

    @Column(nullable = false)
    private String afm ;

    @Column
    private String tenantUsername;




    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @Column(length = 500)
    private String message;

    @Column(nullable = false)
    private String status; // Προεπιλεγμένη τιμή

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "estate_id")
    private Estate estate;



    public Rent() {}

    public Rent(String name, String email, String phone, LocalDateTime appointmentDate, String message, Estate estate, String status, String afm, String identity,String tenantUsername) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.appointmentDate = appointmentDate;
        this.message = message;
        this.estate = estate;
        this.status = status;
        this.afm = afm;
        this.identity = identity;
        this.tenantUsername = tenantUsername;
    }

    // Getters και Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDateTime appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Estate getEstate() {
        return estate;
    }

    public void setEstate(Estate estate) {
        this.estate = estate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
    }

    public String getTenantUsername() {
        return tenantUsername;
    }
    public void setTenantUsername(String tenantUsername) {
        this.tenantUsername = tenantUsername;
    }

    @Override
    public String toString() {
        return "Rent{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", appointmentDate=" + appointmentDate +
                ", message='" + message + '\'' +
                ", estate=" + (estate != null ? estate.getId() : null) +
                ", status='" + status + '\'' +
                ", identity='" + identity + '\'' +
                ", afm='" + afm + '\'' +
                ", tenantUsername='" + tenantUsername + '\'' +
                '}';
    }
}

