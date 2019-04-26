package com.anytec.apigateway.dao;

import com.anytec.apigateway.common.HouseUserType;
import com.anytec.apigateway.common.result.ListResponse;
import com.anytec.apigateway.common.model.*;
import com.anytec.apigateway.common.result.RestResponse;
import com.anytec.apigateway.config.GenericConfig;
import com.anytec.apigateway.utils.Rests;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class HouseDao {

    @Autowired
    private GenericConfig rest;
    
    @Value("${house.service.name}")
    String houseService;


    public ListResponse<House> getHouses(House query, Integer limit, Integer offset) {

       return Rests.exc(() ->{
            HouseQueryReq queryReq = new HouseQueryReq();
            queryReq.setQuery(query);
            queryReq.setLimit(limit);
            queryReq.setOffset(offset);
            String url = Rests.toUrl(houseService,"/house/list");
            RestResponse<ListResponse<House>> restResponse = rest.post(url, queryReq, new ParameterizedTypeReference<RestResponse<ListResponse<House>>>() {
            }).getBody();
            return restResponse;
        }).getResult();

    }

    public List<House> getHotHouse(int recomSize) {

        return Rests.exc(() -> {
            String url = Rests.toUrl(houseService,"/house/hot?size="+recomSize);
            RestResponse<List<House>> restResponse = rest.get(url, new ParameterizedTypeReference<RestResponse<List<House>>>() {
            }).getBody();
            return restResponse;
        }).getResult();

    }

    public House getOneHouse(long id) {

        return Rests.exc(() -> {
            String url = Rests.toUrl(houseService,"/house/detail?id="+id);
            RestResponse<House> restResponse = rest.get(url, new ParameterizedTypeReference<RestResponse<House>>() {
            }).getBody();
            return restResponse;
        }).getResult();
    }

    public void addUserMsg(UserMsg userMsg) {

        Rests.exc(() ->{
            String url = Rests.toUrl(houseService,"/house/addUserMsg");
            RestResponse<Object> body = rest.post(url, userMsg, new ParameterizedTypeReference<RestResponse<Object>>() {
            }).getBody();
            return body;
        });
    }

    public void rating(Long id, Double rating) {

        Rests.exc(() ->{
            String url = Rests.toUrl(houseService, "/house/rating?id=" + id + "&rating=" + rating );
            ResponseEntity<RestResponse<Object>> responseEntity = rest.get(url,new ParameterizedTypeReference<RestResponse<Object>>() {});
            return responseEntity.getBody();
        });
    }

    public List<City> getAllCitys() {

        return   Rests.exc(() ->{
            String url = Rests.toUrl(houseService,"/house/allCitys");
            RestResponse<List<City>> restResponse = rest.get(url, new ParameterizedTypeReference<RestResponse<List<City>>>() {
            }).getBody();
            return restResponse;
        }).getResult();

    }

    public List<Community> getAllCommunitys() {

        RestResponse<List<Community>> resp = Rests.exc(() -> {
            String url = Rests.toUrl(houseService, "/house/allCommunitys" );
            ResponseEntity<RestResponse<List<Community>>> responseEntity = rest.get(url,new ParameterizedTypeReference<RestResponse<List<Community>>>() {});
            return responseEntity.getBody();
        });
        return resp.getResult();

    }

    public void addHouse(House house) {

        Rests.exc(() ->{
            String url = Rests.toUrl(houseService, "/house/add" );
            ResponseEntity<RestResponse<Object>> responseEntity = rest.post(url,house,new ParameterizedTypeReference<RestResponse<Object>>() {});
            return responseEntity.getBody();
        });
    }

    public void bindUser2House(Long houseId, Long userId, boolean bookmark) {

        HouseUserReq req = new HouseUserReq();
        req.setUnBind(false);
        req.setBindType(HouseUserType.BOOKMARK.value);
        req.setUserId(userId);
        req.setHouseId(houseId);
        Rests.exc(() -> {
             String url = Rests.toUrl(houseService, "/house/bind");
             ResponseEntity<RestResponse<Object>> responseEntity = rest.post(url,req,new ParameterizedTypeReference<RestResponse<Object>>() {});
             return responseEntity.getBody();
        });
    }

    public void unbindUser2House(Long houseId, Long userId, boolean bookmark) {

        HouseUserReq req = new HouseUserReq();
        req.setUnBind(true);
        if (bookmark) {
            req.setBindType(HouseUserType.BOOKMARK.value);
        }else {
            req.setBindType(HouseUserType.SALE.value);
        }
        req.setUserId(userId);
        req.setHouseId(houseId);
        Rests.exc(() -> {
            String url = Rests.toUrl(houseService, "/house/bind");
            ResponseEntity<RestResponse<Object>> responseEntity = rest.post(url,req,new ParameterizedTypeReference<RestResponse<Object>>() {});
            return responseEntity.getBody();
        });

    }
}
