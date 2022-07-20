package github.ProudTurtle;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("task")
public class TaskConfigurationProperties {

   private Template template;

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public static class Template {
        private boolean allowMultipleTasksFromTemplate;

        public boolean isAllowMultipleTasksFromTemplate() {
            return allowMultipleTasksFromTemplate;
        }

        public void setAllowMultipleTasksFromTemplate(boolean allowMultipleTasksFromTemplate) {
            this.allowMultipleTasksFromTemplate = allowMultipleTasksFromTemplate;
        }
    }

}
