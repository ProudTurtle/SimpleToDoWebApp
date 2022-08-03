package github.ProudTurtle.controller;

import github.ProudTurtle.model.Task;
import github.ProudTurtle.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class TaskControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repo;

    @Test
    void httpGet_returnsGivenTask() throws Exception {
        int id = repo.save(new Task("foo", LocalDateTime.now())).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/" + id))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }

}
