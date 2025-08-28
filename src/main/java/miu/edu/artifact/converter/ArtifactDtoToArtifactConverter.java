package miu.edu.artifact.converter;

import miu.edu.artifact.Artifact;
import miu.edu.dto.ArtifactDto;
import miu.edu.wizard.converter.WizardDtoToWizardConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactDtoToArtifactConverter implements Converter<ArtifactDto, Artifact> {

  @Autowired
  WizardDtoToWizardConverter wizardDtoToWizardConverter;

  @Override
  public Artifact convert(ArtifactDto source) {
    Artifact artifact = new Artifact();
    artifact.setId(source.id());
    artifact.setName(source.name());
    artifact.setDescription(source.description());
    artifact.setImageUrl(source.imageUrl());
    if(source.owner() != null) {
      artifact.setOwner(wizardDtoToWizardConverter.convert(source.owner()));
    }

    return artifact;
  }
}
