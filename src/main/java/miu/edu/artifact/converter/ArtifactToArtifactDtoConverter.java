package miu.edu.artifact.converter;

import miu.edu.artifact.Artifact;
import miu.edu.dto.ArtifactDto;
import miu.edu.wizard.converter.WizardToWizardDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {

  private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

  public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardToWizardDtoConverter) {
    this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
  }

  @Override
  public ArtifactDto convert(Artifact source) {
    return new ArtifactDto(
        source.getId(),
        source.getName(),
        source.getDescription(),
        source.getImageUrl(),
        source.getOwner() != null ? wizardToWizardDtoConverter.convert(source.getOwner()) : null
    );
  }
}
