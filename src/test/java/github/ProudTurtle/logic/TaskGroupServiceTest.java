package github.ProudTurtle.logic;

import github.ProudTurtle.model.TaskGroup;
import github.ProudTurtle.model.TaskGroupRepository;
import github.ProudTurtle.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskGroupServiceTest {
    @Test
    @DisplayName("should throw when undone tasks")
    void toggleGroup_undoneTasks_throwsIllegalStateException(){
        TaskRepository mockTaskRepository = taskRepositoryReturning(true);
        var toTest = new TaskGroupService(null, mockTaskRepository);
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        assertThat(exception)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("undone tasks");

    }

    @Test
    @DisplayName("should throw when no undone tasks but no group")
    void toggleGroupd_wrongId_throwsIllegalArgumentException(){
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        var mockRepository = mock(TaskGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.empty());
        var toTest = new TaskGroupService(mockRepository, mockTaskRepository);
        var exception = catchThrowable(() -> toTest.toggleGroup(1));

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("id not found");


    }

    @Test
    @DisplayName("should throw when no undone tasks but no group")
    void toggleGroupd_worksAsExpected() throws NoSuchFieldException, IllegalAccessException {
        TaskRepository mockTaskRepository = taskRepositoryReturning(false);
        var group = new TaskGroup();
        var beforeToggled = group.isDone();

        var mockRepository = mock(TaskGroupRepository.class);
        when(mockRepository.findById(anyInt())).thenReturn(Optional.of(group));
        var toTest = new TaskGroupService(mockRepository, mockTaskRepository);

        toTest.toggleGroup(0);

        assertThat(group.isDone()).isEqualTo(!beforeToggled);

    }

    private TaskRepository taskRepositoryReturning(boolean b) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(b);
        return mockTaskRepository;
    }
}