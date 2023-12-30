package com.acrdev.acrcatalog.util;

import com.acrdev.acrcatalog.entities.Product;
import com.acrdev.acrcatalog.projections.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static List<Product> replace(List<ProductProjection> ordered, List<Product> unordered) {
        //pegar ordenação que está na list ProductProjection
        //montar nova lista ordenada com base na list entities que está desordenada

        Map<Long, Product> map = new HashMap<>();

        for(Product obj : unordered){
            map.put(obj.getId(), obj);
        }

        List<Product> result = new ArrayList<>();
        for(ProductProjection obj : ordered){
            result.add(map.get(obj.getId()));
        }

        return result;
    }
}
