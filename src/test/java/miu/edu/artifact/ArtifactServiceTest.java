package miu.edu.artifact;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import miu.edu.artifact.utils.IdWorker;
import miu.edu.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

  @Mock
  private ArtifactRepository artifactRepository;

  @Mock
  private IdWorker idWorker;

  @InjectMocks
  private ArtifactService artifactService;

  List<Artifact> artifacts;

  @BeforeEach
  void setUp() {
    artifacts = new ArrayList<>();
    Artifact staff = new Artifact();
    staff.setId("A1");
    staff.setName("Staff of Eternity");
    staff.setDescription("Emits endless magical energy.");
    staff.setImageUrl("images/staff.png");
    Artifact orb = new Artifact();
    orb.setId("A2");
    orb.setName("Crystal Orb of Time");
    orb.setDescription("Shows glimpses of the past and future.");
    orb.setImageUrl("images/orb.png");
    artifacts.add(staff);
    artifacts.add(orb);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testFindByIdSuccess() {
    //Given
    Artifact ar = new Artifact();
    ar.setId("1250808601744904192");
    ar.setName("Invisibility Cloak");
    ar.setDescription("An invisibility Cloak is used to make the wearer invisible");
    ar.setImageUrl("https://www.invisibilitycloak.com");

    Wizard wizard = new Wizard();
    wizard.setId(2);
    wizard.setName("Harry Potter");
    ar.setOwner(wizard);

    given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(ar));
    //When
    Artifact returnedArtifact = artifactService.findById("1250808601744904192");
    //Then
    assertThat(returnedArtifact.getId()).isEqualTo(ar.getId());
    assertThat(returnedArtifact.getName()).isEqualTo(ar.getName());
    assertThat(returnedArtifact.getDescription()).isEqualTo(ar.getDescription());
    assertThat(returnedArtifact.getImageUrl()).isEqualTo(ar.getImageUrl());
    //we verify this mock object's method is called once inside the service object
    verify(artifactRepository, times(1)).findById("1250808601744904192");
  }

  @Test
  void findByIdNotFound() {
    //Given
    given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());
    //When
    Throwable thrown = catchThrowable(() -> {
      Artifact returnedArtifact = artifactService.findById("1250808601744904192");

    });
    //Then
    assertThat(thrown).
        isInstanceOf(ArtifactNotFoundException.class)
        .hasMessage("Could not find artifact with Id 1250808601744904192");
    //we verify this mock object's method is called once inside the service object
    verify(artifactRepository, times(1)).findById("1250808601744904192");
  }

  @Test
  void findAllArtifactsSuccess() {
    //given
    given(artifactRepository.findAll()).willReturn(artifacts);
    //when
    List<Artifact> returnedArtifacts = artifactService.findAll();
    //then
    assertThat(returnedArtifacts.size()).isEqualTo(artifacts.size());
  }

  @Test
  void testSaveArtifactSuccess() {
    //Given
    Artifact artifact = new Artifact();
    artifact.setName("New Artifact name");
    artifact.setDescription("New Artifact description");
    artifact.setImageUrl("New Artifact Image Url");
    given(idWorker.nextId()).willReturn(123456L);
    given(artifactRepository.save(artifact)).willReturn(artifact);
    //When
    Artifact createdArtifact = artifactService.save(artifact);
    //Then
    assertThat(createdArtifact.getId()).isEqualTo("123456");
    assertThat(createdArtifact.getName()).isEqualTo(artifact.getName());
    assertThat(createdArtifact.getDescription()).isEqualTo(artifact.getDescription());
    assertThat(createdArtifact.getImageUrl()).isEqualTo(artifact.getImageUrl());

    verify(artifactRepository, times(1)).save(artifact);
  }

  @Test
  void testUpdateArtifactSuccess() {
    //Given
    Artifact old = new Artifact();
    old.setId("1250808601744904192");
    old.setName("Invisibility Cloak");
    old.setDescription("An invisibility Cloak is used to make the wearer invisible");
    old.setImageUrl("https://www.invisibilitycloak.com");

    Artifact update = new Artifact();
    update.setId("1250808601744904192");
    update.setName("Invisibility Cloak");
    update.setDescription("New description");
    update.setImageUrl("https://www.invisibilitycloak.com");
    given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(old));
    given(artifactRepository.save(old)).willReturn(old);

    //When
    Artifact updatedArtifact = artifactService.update("1250808601744904192", update);
    //then
    assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
    assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());

    verify(artifactRepository, times(1)).findById("1250808601744904192");
    verify(artifactRepository, times(1)).save(updatedArtifact);
  }

  @Test
  void testUpdateArtifactNotFound() {
    Artifact update = new Artifact();
    update.setName("Invisibility Cloak");
    update.setDescription("New description");
    update.setImageUrl("https://www.invisibilitycloak.com");
    given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

    assertThrows(ArtifactNotFoundException.class, () -> {
      artifactService.update("1250808601744904192", update);
    });

    verify(artifactRepository, times(1)).findById("1250808601744904192");
  }

  @Test
  void testDeleteArtifactSuccess() {
    //Given
    Artifact artifact = new Artifact();
    artifact.setId("123456");
    artifact.setName("New Artifact name");
    artifact.setDescription("New Artifact description");
    artifact.setImageUrl("New Artifact Image Url");
    given(artifactRepository.findById("123456")).willReturn(Optional.of(artifact));
    doNothing().when(artifactRepository).deleteById("123456");
    //When
    artifactService.delete("123456");
    //Then
    verify(artifactRepository, times(1)).deleteById("123456");
  }

  @Test
  void testDeleteArtifactNotFound() {
    //Given
    given(artifactRepository.findById("123456")).willReturn(Optional.empty());
    //When
    assertThrows(ArtifactNotFoundException.class, () -> {
      artifactService.delete("123456");
    });
    //Then
    verify(artifactRepository, times(1)).findById("123456");
  }
}