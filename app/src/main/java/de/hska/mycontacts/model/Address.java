package de.hska.mycontacts.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Domain class for Address
 */
public class Address implements Parcelable {

    /**
     * Empty default constructor for Address
     */
    public Address() {

    }

    /**
     * Constructor for Address
     *
     * @param street  street name
     * @param number  house number
     * @param zipCode zip code
     * @param city    city name
     * @param country country name
     */
    public Address(String street, String number, String zipCode, String city, String country) {
        this.street = street;
        this.number = number;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
    }

    private long id;
    private String street;
    private String number;
    private String zipCode;
    private String city;
    private String country;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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
                "id=" + id +
                "street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }

    /**
     * Describes the content of the Parcel
     *
     * @return bitmask
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Defines how to serialize the object into Parcel
     *
     * @param dest  Parcel in which the object should be stored
     * @param flags additional flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(street);
        dest.writeString(number);
        dest.writeString(zipCode);
        dest.writeString(city);
        dest.writeString(country);
    }

    /**
     * CREATOR field to generate instance of object from Parcel
     */
    public static final Parcelable.Creator<Address> CREATOR
            = new Parcelable.Creator<Address>() {
        /**
         * Creates new instance of Address from Parcel
         * @param in the parcel
         * @return new instance of Address
         */
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        /**
         * Creates new Array of Addresses from Parcel
         * @param size size of the array
         * @return Array with new instances of Addresses
         */
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    /**
     * Private constructor for Address used by CREATOR to deserialize Parcel
     *
     * @param in Parcel to deserialize
     */
    private Address(Parcel in) {
        id = in.readLong();
        street = in.readString();
        number = in.readString();
        zipCode = in.readString();
        city = in.readString();
        country = in.readString();
    }
}
