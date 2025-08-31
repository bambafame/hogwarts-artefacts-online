package miu.edu.wizard;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import miu.edu.artifact.Artifact;

@Entity
public class Wizard implements Serializable {

  @Id
  @GeneratedValue
  private Integer id;
  private String name;
  @OneToMany(mappedBy = "owner", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  List<Artifact>  artifacts = new ArrayList<Artifact>();

  public Wizard() {
  }

  public Wizard(String name) {
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Artifact> getArtifacts() {
    return artifacts;
  }

  public void setArtifacts(List<Artifact> artifacts) {
    this.artifacts = artifacts;
  }

  public void addArtifact(Artifact artifact) {
    artifact.setOwner(this);
    this.artifacts.add(artifact);
  }

  public Integer getNumberOfArtifacts() {
    return artifacts.size();
  }

  public void removeArtifacts() {
    this.artifacts.stream().forEach(ar -> ar.setOwner(null));
    this.artifacts = null;
  }
}
