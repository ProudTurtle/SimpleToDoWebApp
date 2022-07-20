package github.ProudTurtle.adapter;

import github.ProudTurtle.model.Project;
import github.ProudTurtle.model.ProjectRepository;
import github.ProudTurtle.model.TaskGroup;
import github.ProudTurtle.model.TaskGroupRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface SqlProjectRepository extends ProjectRepository, JpaRepository<Project, Integer> {
    @Override
    @Query("select distinct  p from Project p join fetch p.steps")
    List<Project> findAll();

}
