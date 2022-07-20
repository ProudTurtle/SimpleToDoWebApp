package github.ProudTurtle.model;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Embeddable()
 class Audit {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    @PrePersist
    void prePresist(){
        createdOn = LocalDateTime.now();
    }
    @PreUpdate
    void preMerge(){
        updatedOn = LocalDateTime.now();
    }
}
