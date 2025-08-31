package miu.edu.wizard;

import java.util.List;
import java.util.NoSuchElementException;
import miu.edu.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WizardService {

  private final WizardRepository wizardRepository;

  public WizardService(WizardRepository wizardRepository) {
    this.wizardRepository = wizardRepository;
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

}
