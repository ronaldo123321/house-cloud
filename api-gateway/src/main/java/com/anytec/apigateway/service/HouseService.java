package com.anytec.apigateway.service;

import com.anytec.apigateway.common.result.ListResponse;
import com.anytec.apigateway.common.PageData;
import com.anytec.apigateway.common.PageParams;
import com.anytec.apigateway.common.model.*;
import com.anytec.apigateway.dao.HouseDao;
import com.anytec.apigateway.utils.BeanHelper;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class HouseService {
    
    @Autowired
    private HouseDao houseDao;

    @Autowired
    private FileService fileService;

    public PageData<House> queryHouse(House query, PageParams build) {

        ListResponse<House> result = houseDao.getHouses(query, build.getLimit(), build.getOffset());
        return PageData.buildPage(result.getList(),result.getCount(),build.getPageSize(),build.getPageNum());

    }

    public List<House> getHotHouse(int recomSize) {

        List<House> houses = houseDao.getHotHouse(recomSize);
        return houses;
    }

    public House queryOneHouse(long id) {
        return houseDao.getOneHouse(id);
    }

    public void addUserMsg(UserMsg userMsg) {
        houseDao.addUserMsg(userMsg);
    }

    public void updateRating(Long id, Double rating) {
        houseDao.rating(id,rating);
    }

    public List<City> getAllCitys() {
       return  houseDao.getAllCitys();
    }

    public List<Community> getAllCommunitys() {

        return houseDao.getAllCommunitys();
    }

    public void addHouse(House house, User user) {

         if(house.getHouseFiles() != null && !house.getHouseFiles().isEmpty()){
             List<MultipartFile> houseFiles = house.getHouseFiles();
             List<String> imgPath = fileService.getImgPath(houseFiles);
             String imgs = Joiner.on(",").join(imgPath);
             house.setImages(imgs);
         }
        if (house.getFloorPlanFiles() != null && !house.getFloorPlanFiles().isEmpty()) {
            List<MultipartFile> files = house.getFloorPlanFiles();
            String floorPlans = Joiner.on(",").join(fileService.getImgPath(files));
            house.setFloorPlan(floorPlans);
        }
        BeanHelper.setDefaultProp(house,House.class);
        BeanHelper.onInsert(house);
        house.setUserId(user.getId());
        houseDao.addHouse(house);

    }

    public void bindUser2House(Long houseId, Long userId, boolean bookmark) {
         houseDao.bindUser2House(houseId,userId,bookmark);
    }

    public void unbindUser2House(Long houseId, Long userId, boolean bookmark) {
        houseDao.unbindUser2House(houseId,userId,bookmark);
    }
}
