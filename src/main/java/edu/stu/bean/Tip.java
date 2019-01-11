package edu.stu.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "tip")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tip implements Serializable {

    private final static long serialVersionUID = 1L;

    private String id;

    private String name;

    private String district;

    private String location; // 经纬度

    private String address;

    private String typecode;

    private String city;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Tip{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", district='" + district + '\'' +
                ", location='" + location + '\'' +
                ", address='" + address + '\'' +
                ", typecode='" + typecode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
