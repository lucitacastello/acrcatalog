package com.acrdev.acrcatalog.projections;

//- usando OO Genérica
public interface ProductProjection extends IdProjection<Long> {

    //Long getId(); -herança da interface Genérica
    String getName();

}
