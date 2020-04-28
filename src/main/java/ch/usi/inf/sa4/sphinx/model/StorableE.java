package ch.usi.inf.sa4.sphinx.model;

import com.google.gson.annotations.Expose;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class StorableE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    protected Integer id;


    @Expose
    @CreationTimestamp
    private LocalDateTime createDateTime;


    @Expose
    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    public Integer getId() {
        return id;
    }

    //Getters and setters omitted for brevity
}
