package github.ProudTurtle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


@SpringBootApplication
public class SimpleToDoWebAppApplication  {

	public static void main(String[] args) {
		SpringApplication.run(SimpleToDoWebAppApplication.class, args);
	}

	@Bean
	Validator validator(){
		return  new LocalValidatorFactoryBean();
	}
}
