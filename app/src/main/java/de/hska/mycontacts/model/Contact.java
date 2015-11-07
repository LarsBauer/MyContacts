package de.hska.mycontacts.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

/**
 * Domain class for Contact
 */
public class Contact implements Parcelable{

    /**
     * Empty default constructor for Contact
     */
    public Contact(){

    }

    /**
     * Constructor for Contact
     * @param firstName first name
     * @param lastName last name
     * @param phone phone number
     * @param mail mail address
     * @param address address of the contact
     */
    public Contact(String firstName, String lastName, String phone, String mail, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.mail = mail;
        this.address = address;
    }

    private Uri image;
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private Address address;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "image=" + image +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", mail='" + mail + '\'' +
                ", address=" + address +
                '}';
    }

    /**
     * Describes the content of the Parcel
     * @return bitmask
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Defines how to serialize the object into Parcel
     * @param dest Parcel in which the object should be stored
     * @param flags additional flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(image.getPath());
        dest.writeString(phone);
        dest.writeString(mail);
        dest.writeParcelable(address, flags);
    }

    /**
     * CREATOR field to generate instance of object from Parcel
     */
    public static final Parcelable.Creator<Contact> CREATOR
            = new Parcelable.Creator<Contact>() {
        /**
         * Creates new instance of Contact from Parcel
         * @param in the parcel
         * @return new instance of Contact
         */
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        /**
         * Creates new Array of Contacts from Parcel
         * @param size size of the array
         * @return Array with new instances of Contacts
         */
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    /**
     * Private constructor for Contact used by CREATOR to deserialize Parcel
     * @param in Parcel to deserialize
     */
    private Contact(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        image = Uri.parse(in.readString());
        phone = in.readString();
        mail = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());
    }
}
