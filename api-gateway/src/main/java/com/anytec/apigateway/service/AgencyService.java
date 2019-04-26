package com.anytec.apigateway.service;

import com.anytec.apigateway.common.result.ListResponse;
import com.anytec.apigateway.common.model.User;
import com.anytec.apigateway.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class AgencyService {


    @Autowired
    private UserDao userDao;

    public ListResponse<User> getAllAgency() {

      return   userDao.getAllAgency();
    }

    public User getAgentDetail(Long id) {

        return   userDao.getAgentById(id);
    }
}
