package miu.edu.artifact;

import jakarta.validation.Valid;
import java.util.List;
import miu.edu.artifact.converter.ArtifactDtoToArtifactConverter;
import miu.edu.artifact.converter.ArtifactToArtifactDtoConverter;
import miu.edu.dto.ArtifactDto;
import miu.edu.system.Result;
import miu.edu.system.StatusCode;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/artifacts")
public class ArtifactController {

  private final ArtifactService artifactService;
  private final ArtifactToArtifactDtoConverter artifactDtoConverter;
  private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;
  private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;

  public ArtifactController(ArtifactService artifactService,
      ArtifactToArtifactDtoConverter artifactDtoConverter,
      ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter,
      ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter) {
    this.artifactService = artifactService;
    this.artifactDtoConverter = artifactDtoConverter;
    this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
  }

  @GetMapping("/{artifactId}")
  public Result findArtifactById(@PathVariable String artifactId) {
    Artifact artifact = artifactService.findById(artifactId);
    Result result = new Result();
    result.setFlag(true);
    result.setCode(StatusCode.SUCCESS);
    result.setMessage("Find One Success");
    result.setData(artifactDtoConverter.convert(artifact));
    return result;
  }

  @GetMapping
  public Result findAllArtifacts() {
    List<Artifact> artifacts = artifactService.findAll();
    Result result = new Result();
    List<ArtifactDto> artifactDtos = artifacts.stream().map(artifactDtoConverter::convert).toList();
    result.setFlag(true);
    result.setCode(StatusCode.SUCCESS);
    result.setMessage("Find All Success");
    result.setData(artifactDtos);
    return result;
  }

  @PostMapping
  public Result addArtifact(@Valid @RequestBody ArtifactDto artifactDto) {
    Artifact artifact = artifactDtoToArtifactConverter.convert(artifactDto);
    artifact = artifactService.save(artifact);
    Result result = new Result();
    result.setFlag(true);
    result.setCode(StatusCode.SUCCESS);
    result.setMessage("Add Success");
    result.setData(artifactDtoConverter.convert(artifact));
    return result;
  }

  @PutMapping("/{artifactId}")
  public Result updateArtifact(@PathVariable String artifactId, @Valid @RequestBody ArtifactDto artifactDto) {
    Artifact update = artifactDtoToArtifactConverter.convert(artifactDto);
    Artifact updatedArtifact = artifactService.update(artifactId, update);
    ArtifactDto updatedArtifactDto = artifactToArtifactDtoConverter.convert(updatedArtifact);
    return new Result(true, StatusCode.SUCCESS, "Update Success", updatedArtifactDto);
  }

  @DeleteMapping("/{artifactId}")
  public Result deleteArtifact(@PathVariable String artifactId) {
    artifactService.delete(artifactId);
    return new Result(true, StatusCode.SUCCESS, "Delete Success");
  }
}
