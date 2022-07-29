package github.ProudTurtle.logic;

import github.ProudTurtle.TaskConfigurationProperties;
import github.ProudTurtle.model.*;
import github.ProudTurtle.model.projection.GroupReadModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and undone group exists")
    void createGroup_noMultipleGroupsConfig_And_openGroupsExists_throwsIllegalException() {

        TaskGroupRepository mockGroupRepository = groupRepositoryReturning(true);

        TaskConfigurationProperties mockConfig = configurationReturning(false);

        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);

        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("one undone group");

        assertThatThrownBy(() -> toTest.createGroup(LocalDateTime.now(), 0)).isInstanceOf(IllegalStateException.class);

    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configuration ok and no projects for a given id")
    void createGroup_configurationOk_And_noProjects_throwsIllegalArgumentException() {

        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository, null, mockConfig);

        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when configured to allow just 1 group and no groups and")
    void createGroup_noMultipleGroupsConfig_And_noUndoneGroupsExists_noProjects_throwsIllegalArgumentException() {

        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());

        TaskGroupRepository mockGroupRepository =   groupRepositoryReturning(false);

        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository, mockGroupRepository, mockConfig);

        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");
    }

    @Test
    @DisplayName("should create a new group from project")
    void createGroup_configurationOk_existingProject_createsAndSavesGroup(){

        var today = LocalDate.now().atStartOfDay();
        var project = projectWith("bar", Set.of(-1,-2));
        var mockRepository = mock(ProjectRepository.class);
        when(mockRepository.findById(anyInt()))
                .thenReturn(Optional.of(project)
                );

        InMemoryGroupRepository inMemoryGroupRepo = inMemoryGroupRepository();
        var countBeforeCall = inMemoryGroupRepo.count();
        TaskConfigurationProperties mockConfig = configurationReturning(true);

        var toTest = new ProjectService(mockRepository, inMemoryGroupRepo, mockConfig);

        GroupReadModel result = toTest.createGroup(today,1);

        assertThat(result.getDescription()).isEqualTo("bar");
        assertThat(result.getDeadline()).isEqualTo(today.minusDays(1));
        assertThat(result.getTasks()).allMatch(task -> task.getDescription().equals("foo"));
        assertThat(countBeforeCall + 1).isEqualTo(inMemoryGroupRepo.count());
    }

    private Project projectWith(String projectDescription, Set<Integer> daysToDeadline){

        var result = mock(Project.class);
        when(result.getDescription()).thenReturn(projectDescription);
        Set<ProjectStep> steps = daysToDeadline.stream()
                        .map(days -> {
                            var step = mock(ProjectStep.class);
                            when(step.getDescription()).thenReturn("foo");
                            when(step.getDaysToDeadline()).thenReturn(days);
                            return step;
                        }).collect(Collectors.toSet());
        when(result.getSteps()).thenReturn(steps);
        return result;
    }

    private TaskGroupRepository groupRepositoryReturning(boolean result) {
        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(result);
        return mockGroupRepository;
    }

    private TaskConfigurationProperties configurationReturning(boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }

    private InMemoryGroupRepository inMemoryGroupRepository(){return new InMemoryGroupRepository();
    }
    private static class InMemoryGroupRepository implements TaskGroupRepository {
        private int index = 0;
        private Map<Integer, TaskGroup> map = new HashMap<>();

        public int count(){
            return map.values().size();
        }
        @Override
        public List<TaskGroup> findAll() {
            return new ArrayList<>(map.values());
        }

        @Override
        public Optional<TaskGroup> findById(Integer id) {
            return Optional.ofNullable(map.get(id));
        }

        @Override
        public TaskGroup save(TaskGroup entity) throws NoSuchFieldException, IllegalAccessException {
            if(entity.getId() != 0){
                var field = Task.class.getDeclaredField("id");
                field.setAccessible(true);
                field.set(entity, ++index);
            }
            map.put(++index, entity);
            return entity;
        }

        @Override
        public boolean existsByDoneIsFalseAndProject_Id(Integer projectId) {
            return map.values().stream()
                    .filter(group -> !group.isDone())
                    .anyMatch(group -> group.getProject() != null && group.getProject().getId() == projectId);
        }
    }

}