package com.anytec.userservice.model;

import lombok.Data;

@Data
public class PageParams {

    public static final Integer PAGE_SIZE = 2;
    public static final Integer PAGE_NUM = 1;

    private Integer pageSize;

    private Integer pageNum;

    private Integer offset;

    private Integer limit;

    public static PageParams build(Integer pageSize, Integer pageNum){
        if(pageSize == null){
            pageSize = PAGE_SIZE;
        }
        if(pageNum == null){
            pageNum = PAGE_NUM;
        }
        return new PageParams(pageSize,pageNum);
    }

    public PageParams() {
        this(PAGE_SIZE,PAGE_NUM);
    }

    public PageParams(Integer pageSize, Integer pageNum) {
        this.pageSize = pageSize;
        this.pageNum = pageNum;
        this.offset = pageSize * (pageNum -1);
        this.limit = pageSize;
    }
}
