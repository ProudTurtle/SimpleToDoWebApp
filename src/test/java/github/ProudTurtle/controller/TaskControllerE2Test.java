package github.ProudTurtle.controller;

import github.ProudTurtle.model.Task;
import github.ProudTurtle.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2Test {
    @LocalServerPort
    private int port;

    @Autowired
   private TestRestTemplate restTemplate;

   @Autowired
    TaskRepository repo;

    @Test
    void httpGet_returnsAllTasks(){
        repo.save(new Task("foo", LocalDateTime.now()));
        repo.save(new Task("bar", LocalDateTime.now()));

       Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

       assertThat(result).hasSize(2);
    }
}