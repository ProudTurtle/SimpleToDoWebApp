package github.ProudTurtle.logic;

import github.ProudTurtle.model.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    private TaskRepository taskRepositoryReturning(boolean b) {
        var mockTaskRepository = mock(TaskRepository.class);
        when(mockTaskRepository.existsByDoneIsFalseAndGroup_Id(anyInt())).thenReturn(b);
        return mockTaskRepository;
    }

    @Test
    @DisplayName("should throw when no undone tasks but no group")
    void toggleGroupd_wrongId_throwsIllegalArgumentException(){
        
    }
}