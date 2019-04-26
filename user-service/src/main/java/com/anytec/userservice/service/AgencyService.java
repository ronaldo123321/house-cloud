package com.anytec.userservice.service;

import com.anytec.userservice.mapper.AgencyMapper;
import com.anytec.userservice.model.PageParams;
import com.anytec.userservice.model.User;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgencyService {


    @Value("${file.prefix}")
    String filePrefix;

    @Autowired
    private AgencyMapper agencyMapper;

    public Pair<List<User>, Long> getAllAgent(PageParams pageParams) {

        List<User> agents = agencyMapper.selectAgent(new User(), pageParams);
        setImg(agents);
        Long count = agencyMapper.selectAgentCount(new User());
        return ImmutablePair.of(agents,count);

    }

    private void setImg(List<User> agents) {

        agents.forEach(u -> {
            u.setAvatar(filePrefix + u.getAvatar());
        });
    }

    public User getAgentDetail(Long id){
        User user = new User();
        user.setId(id);
        user.setType(2);
        List<User> list = agencyMapper.selectAgent(user, new PageParams(1, 1));
        setImg(list);
        if(list.isEmpty()){
            return null;
        }
        return list.get(0);

    }



}
