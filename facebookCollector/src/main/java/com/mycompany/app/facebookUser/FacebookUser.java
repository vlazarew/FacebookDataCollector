package com.mycompany.app.facebookUser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FacebookUser {

    private String firstName;
    private String lastName;
    private String photoURL;
    private String placeOfWork;
    private String placeOfStudy;
    private String email;
    private String phoneNumber;
    private String profileURL;
}
