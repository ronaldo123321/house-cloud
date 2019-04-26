package com.anytec.houseservice.dao;

import com.anytec.houseservice.common.RestResponse;
import com.anytec.houseservice.config.GenericConfig;
import com.anytec.houseservice.model.User;
import com.anytec.houseservice.utils.Rests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    @Autowired
    private GenericConfig rest;

    @Value("${user.service.name}")
    private String userServiceName;

    public User getAgentDetail(Long agentId) {

        return    Rests.exc(() ->{
            String url = Rests.toUrl(userServiceName, "/agency/agentDetail" + "?id=" + agentId);
            RestResponse<User> restResponse = rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {
            }).getBody();
            return restResponse;
        }).getResult();
    }
}
