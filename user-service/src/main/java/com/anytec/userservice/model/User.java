package com.anytec.userservice.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * @author anytec-gb
 */
@Data
public class User implements Serializable {

    private Long id;

    private String name;

    private String phone;

    private String email;

    private String passwd;

    private String confirmPasswd;

    private Integer type;

    private Date createTime;

    private Integer enable;

    private String avatar;

    @JSONField(deserialize = false,serialize = false)
    private MultipartFile avatarFile;

    private String newPassword;

    private String key;

    private String aboutme;

    private Long agencyId;

    private String token;

    private String enableUrl;

    private String agencyName;







}
