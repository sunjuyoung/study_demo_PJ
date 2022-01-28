package com.project.study.dto;

import com.project.study.domain.Account;
import lombok.Data;

import javax.persistence.Lob;

@Data
public class ProfileForm {

    private String bio;
    private String url;
    private String occupation;
    private String location;


    private String profileImage;

    public ProfileForm(Account account){
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
        this.profileImage = account.getProfileImage();
    }
}
