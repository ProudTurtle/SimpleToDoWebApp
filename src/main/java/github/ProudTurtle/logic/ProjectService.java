package github.ProudTurtle.logic;

import github.ProudTurtle.TaskConfigurationProperties;
import github.ProudTurtle.model.*;
import github.ProudTurtle.model.projection.GroupReadModel;
import github.ProudTurtle.model.projection.GroupTaskWriteModel;
import github.ProudTurtle.model.projection.GroupWriteModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//@Service
public class ProjectService {
    private  ProjectRepository repository;
    private  TaskGroupRepository taskGroupRepository;
    private  TaskConfigurationProperties config;
    private TaskGroupService taskGroupService;


    public ProjectService(final ProjectRepository repository, TaskGroupRepository taskGroupRepository,
                          TaskConfigurationProperties config, TaskGroupService service) {
        this.repository = repository;
        this.taskGroupRepository = taskGroupRepository;
        this.config = config;
        this.taskGroupService = service;
    }

    public List<Project> readAll() {
        return repository.findAll();
    }

    public Project save(Project toSave) {
        return repository.save(toSave);
    }

    public GroupReadModel createGroup(LocalDateTime deadline, int projectId) {
        if (!config.getTemplate().isAllowMultipleTasks() && taskGroupRepository.existsByDoneIsFalseAndProject_Id(projectId)) {
            throw new IllegalStateException("Only one undone group from project is allowed");
        }
        GroupReadModel result = repository.findById(projectId)
                .map(project -> {
                    var targetGroup = new GroupWriteModel();
                    targetGroup.setDescription(project.getDescription());
                    targetGroup.setTasks(
                            project.getSteps().stream()
                                    .map(projectStep -> {
                                             var task = new GroupTaskWriteModel();
                                             task.setDescription(projectStep.getDescription());
                                             task.setDeadline(deadline.plusDays(projectStep.getDaysToDeadline()));
                                             return task;
                                            }
                                    ).collect(Collectors.toSet())
                    );
                    try {
                       return taskGroupService.createGroup(targetGroup, project);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                }).orElseThrow(() -> new IllegalArgumentException("Project with given id not found"));
        return result;
    }
}
