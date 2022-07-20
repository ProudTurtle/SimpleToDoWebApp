//package github.ProudTurtle.logic;
//
//import github.ProudTurtle.adapter.TaskGroupRepository;
//import github.ProudTurtle.model.Task;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class TempService {
//    @Autowired
//    List<String> temp(TaskGroupRepository repository){
//       return repository.findAll().stream()
//                .flatMap(taskGroup ->
//            taskGroup.getTasks().stream()).map(Task::getDescription)
//                .collect(Collectors.toList());
//        }
//    }
//
