package gr.hua.dit.rentEstate.entities;

import jakarta.persistence.*;

@Entity
@Table
public class Estate {

    @Column
    private int floor;

    @Column
    private int price;

    @Column
    private String cityName;

    @Column
    private String areaName;

    @Column
    private int yearBuilt;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int bedrooms;

    @Column
    private int bathrooms;

    @Column
    private int sqM;

    @Column
    private int streetNumber;

    @Column
    private String streetName;

    @Column
    private int renovationYear;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = false;



    public Boolean isApproved() {
        return isApproved;
    }

    public void setApproved(Boolean isApproved) {
        this.isApproved = isApproved;

    }
    public String getStatus() {
        return (isApproved != null && isApproved) ? "Approved By Admin" : "Not-Approved By Admin";
    }

    public Estate(int floor, int price, String cityName, String areaName, int yearBuilt, int id,
                     int bedrooms, int bathrooms, int sqM, int streetNumber, String streetName, int renovationYear) {
        this.floor = floor;
        this.price = price;
        this.cityName = cityName;
        this.areaName = areaName;
        this.yearBuilt = yearBuilt;
        this.id = id;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.sqM = sqM;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.renovationYear = renovationYear;
    }

    public Estate() {

    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="owner_id")
    private Owner owner;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name="tenant_id")
    private Tenant tenant;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getYearBuilt() {
        return yearBuilt;
    }

    public void setYearBuilt(int yearBuilt) {
        this.yearBuilt = yearBuilt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public int getSqM() {
        return sqM;
    }

    public void setSqM(int sqM) {
        this.sqM = sqM;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getRenovationYear() {
        return renovationYear;
    }

    public void setRenovationYear(int renovationYear) {
        this.renovationYear = renovationYear;
    }

    public void setOwner(Owner owner){
            this.owner=owner;
    }
    public Owner getOwner(){
        return owner;
    }

    @Column
    private String ownerName;

    public void setOwnerName(String ownerName){this.ownerName=ownerName;}
    public String getOwnerName(){return ownerName;}


    public void setTenant(Tenant tenant){
        this.tenant = tenant;
    }
    public Tenant getTenant(){
        return tenant;
    }



    @Override
    public String toString() {
        return "Estate{" +
                "Id=" + id +
                ", floor='" + floor + '\'' +
                ", yearBuilt='" + yearBuilt + '\'' +
                ", bedrooms='" + bedrooms + '\'' +
                ", bathrooms='" + bathrooms + '\'' +
                ", Area='" + areaName + '\'' +
                ", city='" + cityName + '\'' +
                ", price='" + price + '\'' +
                ", sqm='" + sqM + '\'' +
                ", Street number='" + streetNumber + '\'' +
                ", Street Name='" + streetName + '\'' +
                ", renovationYear='" + renovationYear + '\'' +
                ", owner=" + (owner != null ? ownerName : "No Owner") +
                '}';
    }
}

