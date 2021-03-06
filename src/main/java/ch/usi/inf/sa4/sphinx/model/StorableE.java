package ch.usi.inf.sa4.sphinx.model;

import com.google.gson.annotations.Expose;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Parent class for all models.
 * Contains the creation date of the model, the update date of a stored object and the unique id associated to it.
 */
@MappedSuperclass
public abstract class StorableE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    protected Integer id;

    /**
     * Gets the unique id associated to this entity.
     * @return the id or null if not yet stored
     */
    public Integer getId() {
        return id;
    }

    //Getters and setters omitted for brevity
}
