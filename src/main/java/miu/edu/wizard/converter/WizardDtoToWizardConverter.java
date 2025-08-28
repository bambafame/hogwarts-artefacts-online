package miu.edu.wizard.converter;

import miu.edu.dto.WizardDto;
import miu.edu.wizard.Wizard;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard> {

  @Override
  public Wizard convert(WizardDto source) {
    Wizard wizard = new Wizard();
    wizard.setId(source.id());
    wizard.setName(source.name());
    return wizard;
  }
}
