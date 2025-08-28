package miu.edu.dto;

import jakarta.validation.constraints.NotEmpty;

public record ArtifactDto(String id,
                          @NotEmpty(message = "Name is required.") String name,
                          @NotEmpty(message = "Description is required.") String description,
                          @NotEmpty(message = "Image url is required.") String imageUrl,
                          WizardDto owner) {

}
