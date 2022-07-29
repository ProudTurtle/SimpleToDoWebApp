package github.ProudTurtle.logic;



import github.ProudTurtle.model.TaskGroup;
import github.ProudTurtle.model.TaskGroupRepository;
import github.ProudTurtle.model.TaskRepository;
import github.ProudTurtle.model.projection.GroupReadModel;
import github.ProudTurtle.model.projection.GroupWriteModel;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class TaskGroupService {
    private final TaskGroupRepository repository;
    private final TaskRepository  taskRepository;

    TaskGroupService(final TaskGroupRepository repository, final TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public GroupReadModel createGroup(GroupWriteModel source) throws NoSuchFieldException, IllegalAccessException {
        TaskGroup result = repository.save(source.toGroup());
        return new GroupReadModel(result);
    }

    public List<GroupReadModel> readAll(){
       return repository.findAll()
                .stream()
                .map(GroupReadModel::new)
                .collect(Collectors.toList());
    }

    public void toggleGroup(int groupId) throws NoSuchFieldException, IllegalAccessException {
        if(taskRepository.existsByDoneIsFalseAndGroup_Id(groupId)){
            throw new IllegalStateException("Group has undone tasks. Done all the task first!");
        }
        TaskGroup result= repository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Task group with given id not found"));
        result.setDone(!result.isDone());
        repository.save(result);
    }
}

