package miu.edu.wizard.converter;

import miu.edu.dto.WizardDto;
import miu.edu.wizard.Wizard;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardToWizardDtoConverter implements Converter<Wizard, WizardDto> {

  @Override
  public WizardDto convert(Wizard source) {
    return new WizardDto(source.getId(), source.getName(), source.getNumberOfArtifacts());
  }
}
