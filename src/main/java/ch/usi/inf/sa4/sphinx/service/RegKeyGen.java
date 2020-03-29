package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Storable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


public class RegKeyGen extends KeyGen<Integer, Storable<Integer>>{
    private Integer id;

    public RegKeyGen(){
        id = 1;
    }


    @Override
    public Integer generateKey(Storable<Integer> item) {
        return id++;
    }
}
