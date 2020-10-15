package com.mycompany.app.facebookUser;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Getter
@Setter
public class FacebookUser {

    @JsonProperty("Firstname")
    private String firstName;

    @JsonProperty("Lastname")
    private String lastName;

    @JsonProperty("PhotoURL")
    private String photoURL;

    @JsonProperty("PlaceOfWork")
    private String placeOfWork;

    @JsonProperty("PlaceOfStudy")
    private String placeOfStudy;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("PhoneNumber")
    private String phoneNumber;

    @JsonProperty("ProfileURL")
    private String profileURL;

    @JsonProperty("HomePlace")
    private String homePlace;

    @JsonProperty("Website")
    private String website;

    @JsonProperty("ID")
    private String UID;

    public FacebookUser(String firstName, String lastName, String photoURL, String placeOfWork, String placeOfStudy,
                        String email, String phoneNumber, String profileURL, String homePlace, String website) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoURL = photoURL;
        this.placeOfWork = placeOfWork;
        this.placeOfStudy = placeOfStudy;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profileURL = profileURL;
        this.homePlace = homePlace;
        this.website = website;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        this.UID = firstName + lastName + timeStamp;
    }

    public FacebookUser() {
        this.firstName = "";
        this.lastName = "";
        this.photoURL = "";
        this.placeOfWork = "";
        this.placeOfStudy = "";
        this.email = "";
        this.phoneNumber = "";
        this.profileURL = "";
        this.homePlace = "";
        this.website = "";
    }
}
