package miu.edu.wizard;

import java.util.List;
import java.util.NoSuchElementException;
import miu.edu.artifact.Artifact;
import miu.edu.artifact.ArtifactRepository;
import miu.edu.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WizardService {

  private final WizardRepository wizardRepository;
  private final ArtifactRepository artifactRepository;

  public WizardService(WizardRepository wizardRepository, ArtifactRepository artifactRepository) {
    this.wizardRepository = wizardRepository;
    this.artifactRepository = artifactRepository;
  }

  public  Wizard findById(Integer id) {
    return wizardRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Wizard", String.valueOf(id)));
  }

  public List<Wizard> findAll() {
    return wizardRepository.findAll();
  }

  public Wizard save(Wizard wizard) {
    return wizardRepository.save(wizard);
  }

  public Wizard update(Integer id, Wizard wizard) {
     return wizardRepository.findById(id).map(oldWizard -> {
       oldWizard.setName(wizard.getName());
       return wizardRepository.save(oldWizard);
     })
         .orElseThrow(() -> new ObjectNotFoundException("Wizard", String.valueOf(id)));
  }

  public void delete(Integer id) {
    Wizard wizard = findById(id);
    wizard.removeArtifacts();
    wizardRepository.deleteById(id);
  }

  public void assignArtifact(Integer wizardId, String artifactId) {
    Artifact artifactToBeAssigned = artifactRepository.findById(artifactId).orElseThrow(() -> new ObjectNotFoundException("Artifact", String.valueOf(artifactId)));

    Wizard wizard = findById(wizardId);
    //We need to see if the artifact is already owned by a wizard
    if(artifactToBeAssigned.getOwner() != null) {
      artifactToBeAssigned.getOwner().removeArtifact(artifactToBeAssigned);
    }
    wizard.addArtifact(artifactToBeAssigned);
  }

}
