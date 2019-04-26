package com.anytec.houseservice.service;

import com.anytec.houseservice.common.LimitOffset;
import com.anytec.houseservice.model.House;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecommendService {

    public static final String HOT_HOUSE_KEY = "hot_house";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private HouseService houseService;

    /**
     * 利用redis 的zset的数据结构 ，用户每点击一次，对应的houseId就增加一分
     * 支持正反序排序，取出指定数量前n个值
     * @param size
     * @return
     */
    public List<House> getHotHouse(Integer size) {

        if(size == null){
            size = 3;
        }
        Set<String> idSet = redisTemplate.opsForZSet().reverseRange(HOT_HOUSE_KEY, 0, size - 1);
        List<Long> ids = idSet.stream().map(b -> Long.parseLong(b)).collect(Collectors.toList());
        log.info("ids={}",ids.toString());
        House house = new House();
        house.setIds(ids);
        final List<Long> orderList = ids;
        List<House> houses = houseService.queryAndSetImg(house, LimitOffset.build(size, 0));
        Ordering<House> houseSort = Ordering.natural().onResultOf(hs -> {
            return orderList.indexOf(hs.getId());
        });
        return houseSort.sortedCopy(houses);

    }

    /**
     * 累计热度
     * @param id
     */
    public void increaseHot(Long id){
        redisTemplate.opsForZSet().incrementScore(HOT_HOUSE_KEY,""+id,1.0D);
        //保留前10个元素，防止太大的内存开销
        redisTemplate.opsForZSet().removeRange(HOT_HOUSE_KEY,0,-11);

    }

    public List<House> getLastest() {
         House house = new House();
         house.setSort("create_time");
         return houseService.queryAndSetImg(house,LimitOffset.build(8,0));
    }
}
