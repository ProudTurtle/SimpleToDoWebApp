package github.ProudTurtle.model.projection;

import github.ProudTurtle.model.Task;
import github.ProudTurtle.model.TaskGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GroupReadModelTest {
    @Test
    @DisplayName("should create null deadline for group when no task deadlines")
    void constructor_noDeadlines_createsNullDeadline(){
        var source = new TaskGroup();
        source.setDescription("foo");
        source.setTasks(Set.of(new Task("bar", null)));

        var result = new GroupReadModel(source);

        assertThat(result).hasFieldOrPropertyWithValue("deadline", null);
    }

}