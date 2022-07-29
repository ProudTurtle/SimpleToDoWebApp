package github.ProudTurtle.logic;

import github.ProudTurtle.TaskConfigurationProperties;
import github.ProudTurtle.model.ProjectRepository;
import github.ProudTurtle.model.TaskGroupRepository;
import github.ProudTurtle.model.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfigration {
    @Bean
    ProjectService service(final ProjectRepository repository,
                           final TaskGroupRepository taksGrouprepository,
                           final TaskConfigurationProperties config){
       return new ProjectService(repository, taksGrouprepository, config);
    }
    @Bean
    TaskGroupService taskGroupService(final TaskGroupRepository repository,
                                      final TaskRepository taskRepository){
        return new TaskGroupService(repository, taskRepository);
    }

}
