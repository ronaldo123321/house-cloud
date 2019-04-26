package com.anytec.apigateway.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Agency implements Serializable {
 
  private Integer id;
  private String  name;
  private String  address;
  private String  phone;
  private String  email;
  private String  aboutUs;
  private String  webSite;
  private String  mobile;
  

  
  

}
