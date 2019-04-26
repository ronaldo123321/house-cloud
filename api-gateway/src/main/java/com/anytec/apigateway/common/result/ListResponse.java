package com.anytec.apigateway.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListResponse<T> {

    private List<T> list;

    private Long count;

    public static <T> ListResponse<T> build(List<T> list,Long count){
        ListResponse<T> listResponse = new ListResponse<>();
        listResponse.setList(list);
        listResponse.setCount(count);
        return listResponse;

    }
}
