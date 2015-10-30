package de.hska.mycontacts.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by larsbauer on 26.10.15.
 */
public class Address implements Parcelable{

    public Address(){

    }

    public Address(String street, String number, String zipCode, String city, String country) {
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }

    private String street;
    private String number;
    private String zipCode;
    private String city;
    private String country;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(street);
        dest.writeString(number);
        dest.writeString(zipCode);
        dest.writeString(city);
        dest.writeString(country);
    }

    public static final Parcelable.Creator<Address> CREATOR
            = new Parcelable.Creator<Address>() {
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    private Address(Parcel in) {
        street = in.readString();
        number = in.readString();
        zipCode = in.readString();
        city = in.readString();
        country = in.readString();
    }
}
