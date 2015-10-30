package de.hska.mycontacts.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

/**
 * Created by larsbauer on 26.10.15.
 */
public class Contact implements Parcelable{

    public Contact(){

    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(image.getPath());
        dest.writeString(phone);
        dest.writeString(mail);
        dest.writeParcelable(address, flags);
    }

    public static final Parcelable.Creator<Contact> CREATOR
            = new Parcelable.Creator<Contact>() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    private Contact(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        image = Uri.parse(in.readString());
        phone = in.readString();
        mail = in.readString();
        address = in.readParcelable(Address.class.getClassLoader());
    }
}
