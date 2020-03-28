package ch.usi.inf.sa4.sphinx.service;

import ch.usi.inf.sa4.sphinx.model.Storable;


public class RegKeyGen extends KeyGen<Integer, Storable>{
    private Integer id;

    public RegKeyGen(){
        id = 1;
    }


    @Override
    public Integer generateKey(Storable item) {
        return id++;
    }
}
