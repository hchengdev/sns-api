package com.snsapi.user;

import lombok.Builder;
import lombok.Data;
import org.apache.tomcat.util.http.fileupload.util.mime.MimeUtility;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@Builder
public class UserForm {
    private String name;
    private Gender gender;
    private Integer phone;
    private String biography;
    private Date birthday;
    private String address;
    private Boolean active;
    private MultipartFile profilePicture;
}
