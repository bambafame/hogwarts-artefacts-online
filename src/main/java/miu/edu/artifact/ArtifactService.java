package miu.edu.artifact;

import java.util.List;
import miu.edu.artifact.utils.IdWorker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ArtifactService {

  private final ArtifactRepository artifactRepository;
  private final IdWorker idWorker;

  public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
    this.artifactRepository = artifactRepository;
    this.idWorker = idWorker;
  }

  public Artifact findById(String id) {
    return artifactRepository.findById(id)
        .orElseThrow(() -> new ArtifactNotFoundException(id));
  }

  public List<Artifact> findAll() {
    return artifactRepository.findAll();
  }

  public Artifact save(Artifact artifact) {
    artifact.setId(String.valueOf(idWorker.nextId()));
    return artifactRepository.save(artifact);
  }

  public Artifact update(String artifactId, Artifact artifact) {
    return artifactRepository.findById(artifactId).map(oldArtifact -> {
      oldArtifact.setDescription(artifact.getDescription());
      oldArtifact.setName(artifact.getName());
      oldArtifact.setImageUrl(artifact.getImageUrl());
      oldArtifact.setOwner(artifact.getOwner());
      return artifactRepository.save(oldArtifact);
    })
        .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
  }

  public void delete(String artifactId) {
    Artifact artifact = findById(artifactId);
    artifactRepository.deleteById(artifactId);
  }
}
