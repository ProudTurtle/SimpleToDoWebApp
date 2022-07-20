package github.ProudTurtle.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Project's description must not be empty")
    private String description;
    @OneToMany(mappedBy = "project")
    private Set<TaskGroup> groups;
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "project")
    private Set<ProjectStep> steps;

    public String getDescription() {
        return description;
    }

     void setDescription(String description) {
        this.description = description;
    }

     public int getId() {
        return id;
    }

     void setId(int id) {
        this.id = id;
    }

     Set<TaskGroup> getGroups() {
        return groups;
    }

     Set<ProjectStep> getSteps() {
        return steps;
    }

     void setSteps(Set<ProjectStep> steps) {
        this.steps = steps;
    }

    void setGroups(Set<TaskGroup> groups) {
        this.groups = groups;


    }
}
