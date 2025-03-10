package gr.hua.dit.rentEstate.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;



    @Column(
            name = "tenantUsername"
    )
    private String tenantUsername;




    @Column(
            name = "email"
    )
    private String email;




    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL)
    private List<Estate> estates;


    public List<Estate> getEstates() {
        return estates;
    }


    public void setEstates(List<Estate> estates) {
        this.estates = estates;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTenantUsername(String tenantUsername) {this.tenantUsername = tenantUsername;}
    public String getTenantUsername() {return tenantUsername;}

    @Override
    public String toString() {
        return "Tenant{" +
                ", email='" + email + '\'' +
                ", id=" + id +
                ", username=" + tenantUsername +
                '}';
    }

}