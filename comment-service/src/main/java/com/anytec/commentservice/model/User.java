package com.anytec.commentservice.model;

import java.util.Date;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class User {
  
  private Long id;
  private String  name;
  private String  phone;
  private String  email;
  private String  aboutme;
  private String  passwd;
  private String  confirmPasswd;
  private Integer type;
  private Date    createTime;
  private Integer enable;
  
  private String  avatar;
  
  private MultipartFile avatarFile;
  
  private String newPassword;
  
  private String key;
  
  private Long  agencyId;

}
