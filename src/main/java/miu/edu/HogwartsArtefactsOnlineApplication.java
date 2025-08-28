package miu.edu;

import miu.edu.artifact.Artifact;
import miu.edu.artifact.ArtifactRepository;
import miu.edu.artifact.utils.IdWorker;
import miu.edu.wizard.Wizard;
import miu.edu.wizard.WizardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HogwartsArtefactsOnlineApplication{

  public static void main(String[] args) {
    SpringApplication.run(HogwartsArtefactsOnlineApplication.class, args);
  }

  @Bean
  public IdWorker idWorker() {
    return new IdWorker(1, 1);
  }

}
