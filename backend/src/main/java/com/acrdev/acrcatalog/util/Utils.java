package com.acrdev.acrcatalog.util;

import com.acrdev.acrcatalog.projections.IdProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//refatorando para usar OO Generics
public class Utils {

    public static <ID> List<? extends IdProjection<ID>> replace(List<? extends IdProjection<ID>> ordered, List<? extends IdProjection<ID>> unordered) {
        //pegar ordenação que está na list ProductProjection
        //montar nova lista ordenada com base na list entities que está desordenada

        Map<ID, IdProjection<ID>> map = new HashMap<>();

        for(IdProjection<ID> obj : unordered){
            map.put(obj.getId(), obj);
        }

        List<IdProjection<ID>> result = new ArrayList<>();
        for(IdProjection<ID> obj : ordered){
            result.add(map.get(obj.getId()));
        }

        return result;
    }
}
