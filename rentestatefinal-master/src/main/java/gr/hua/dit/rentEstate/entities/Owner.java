package gr.hua.dit.rentEstate.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table
public class Owner{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;



    @Column(
            name = "ownerUsername"
    )
    private String ownerUsername;




    @Column(
            name = "email"
    )
    private String email;



    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
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

    public void setOwnerUsername(String ownerUsername) {this.ownerUsername = ownerUsername;}
    public String getOwnerUsername() {return ownerUsername;}

    @Override
    public String toString() {
        return "Owner{" +
                ", email='" + email + '\'' +
                ", id=" + id +
                ", username=" + ownerUsername +
                '}';
    }


}
