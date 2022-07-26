package github.ProudTurtle.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface TaskRepository {
    Page<Task> findAll(Pageable page);
    List<Task> findAll();
    List<Task> findByDone(@Param("state") boolean done);
    Optional<Task> findById( Integer id);
    boolean existsById(Integer id);
    boolean existsByDoneIsFalseAndGroup_Id(Integer groupId);
    Task save (Task entity);
    List<Task> findAllByGroup_Id(Integer groupId);
    List<Task> findAllByDoneIsFalseAndDeadlineIsLessThanEqualOrDoneIsFalseAndDeadlineIsNull(LocalDateTime dateTime);

}
