package github.ProudTurtle.logic;

import github.ProudTurtle.TaskConfigurationProperties;
import github.ProudTurtle.model.ProjectRepository;
import github.ProudTurtle.model.TaskGroup;
import github.ProudTurtle.model.TaskGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectServiceTest {

    @Test
    @DisplayName("should throw IllegalStateException when configured to allow just 1 group and undone group exists")
    void createGroup_noMultipleGroupsConfig_And_openGroupsExists_throwsIllegalException() {

        var mockGroupRepository = mock(TaskGroupRepository.class);
        when(mockGroupRepository.existsByDoneIsFalseAndProject_Id(anyInt())).thenReturn(true);

        TaskConfigurationProperties mockConfig = configurationReturning(false);

        var toTest = new ProjectService(null, mockGroupRepository, mockConfig);

        var exception = catchThrowable(() -> toTest.createGroup(LocalDateTime.now(), 0));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                        .hasMessageContaining("one undone group");

        assertThatThrownBy(() -> {
            toTest.createGroup(LocalDateTime.now(), 0);
        }).isInstanceOf(IllegalStateException.class);

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

    private TaskConfigurationProperties configurationReturning(boolean result) {
        var mockTemplate = mock(TaskConfigurationProperties.Template.class);
        when(mockTemplate.isAllowMultipleTasks()).thenReturn(result);
        var mockConfig = mock(TaskConfigurationProperties.class);
        when(mockConfig.getTemplate()).thenReturn(mockTemplate);
        return mockConfig;
    }
}