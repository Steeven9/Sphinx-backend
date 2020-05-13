package ch.usi.inf.sa4.sphinx.model;

import ch.usi.inf.sa4.sphinx.misc.NotFoundException;
import ch.usi.inf.sa4.sphinx.misc.NotImplementedException;
import ch.usi.inf.sa4.sphinx.misc.UnauthorizedException;

import javax.persistence.Entity;

@Entity
public class Scene extends StorableE implements Runnable {
    @Override
    public void run() {
        throw new NotImplementedException();
    }

    public User getUser(){
        throw new NotImplementedException();
    }
}
