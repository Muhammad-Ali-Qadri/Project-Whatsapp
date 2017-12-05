package com.example.muhammadaliqadri.whatsapp.Model;

/**
 * Created by Muhammad Ali Qadri on 03/12/2017.
 */

public class FirebaseWhatsappUser extends WhatsappUser {
    private String profilePhotoUri;

    public FirebaseWhatsappUser() {

    }

    public FirebaseWhatsappUser(String userId, String userName, String status, String phoneNumber, String profilePhotoUri) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.phoneNumber = phoneNumber;
        this.profilePhotoUri = profilePhotoUri;
    }

    public String getProfilePhotoUri() {
        return profilePhotoUri;
    }

    public void setProfilePhotoUri(String profilePhotoUri) {
        this.profilePhotoUri = profilePhotoUri;
    }
}
