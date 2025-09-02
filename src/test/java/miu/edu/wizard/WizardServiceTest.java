package miu.edu.wizard;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import miu.edu.artifact.Artifact;
import miu.edu.artifact.ArtifactRepository;
import miu.edu.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
  @Mock
  private WizardRepository wizardRepository;
  @Mock
  private ArtifactRepository artifactRepository;
  @InjectMocks
  private WizardService wizardService;
  List<Wizard> wizards;

  @BeforeEach
  void setUp() {
    wizards = new ArrayList<>();
    Wizard wizard1 = new Wizard();
    wizard1.setId(1);
    wizard1.setName("New Wizard 1");
    Wizard wizard2 = new Wizard();
    wizard2.setId(2);
    wizard2.setName("New Wizard 2");
    Wizard wizard3 = new Wizard();
    wizard3.setId(3);
    wizard3.setName("New Wizard 3");
    wizards.add(wizard1);
    wizards.add(wizard2);
    wizards.add(wizard3);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void testFindWizardByIdSuccess() {
    //Given
    Wizard wizard = new Wizard();
    wizard.setId(1);
    wizard.setName("New Wizard");
    given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
    //When
    Wizard foundWizard = wizardService.findById(1);
    //Then
    assertThat(foundWizard.getId()).isEqualTo(wizard.getId());
    assertThat(foundWizard.getName()).isEqualTo(wizard.getName());
    verify(wizardRepository, times(1)).findById(1);
  }

  @Test
  void testFindWizardByIdNotFound() {
    //Given
    given(wizardRepository.findById(1)).willReturn(Optional.empty());

    //When
    Throwable throwable = catchThrowable(() -> wizardService.findById(1));
    //Then
    assertThat(throwable).isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find Wizard with Id 1");
    verify(wizardRepository, times(1)).findById(1);
  }

  @Test
  void testFindAllWizardsSuccess() {
    //Given
    given(wizardRepository.findAll()).willReturn(wizards);
    //When
    List<Wizard> foundWizards = wizardService.findAll();
    //Then
    assertThat(foundWizards.size()).isEqualTo(wizards.size());
    verify(wizardRepository, times(1)).findAll();
  }

  @Test
  void testSaveWizardSuccess() {
    //Given
    Wizard wizard = new Wizard();
    wizard.setId(1);
    wizard.setName("New Wizard");
    given(wizardRepository.save(any(Wizard.class))).willReturn(wizard);
    //when
    Wizard savedWizard = wizardService.save(wizard);
    //then
    assertThat(savedWizard.getId()).isEqualTo(1);
    assertThat(savedWizard.getName()).isEqualTo(wizard.getName());
    verify(wizardRepository, times(1)).save(any(Wizard.class));
  }

  @Test
  void testUpdateWizardSuccess() {
    Wizard wizard = new Wizard();
    wizard.setId(1);
    wizard.setName("New Wizard");

    Wizard update = new Wizard();
    update.setId(1);
    update.setName("Wizard updated");
    given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
    given(wizardRepository.save(wizard)).willReturn(wizard);
    //When
    Wizard updatedWizard = wizardService.update(1, update);
    //Then
    assertThat(updatedWizard.getId()).isEqualTo(1);
    assertThat(updatedWizard.getName()).isEqualTo("Wizard updated");
    verify(wizardRepository, times(1)).findById(1);
    verify(wizardRepository, times(1)).save(any(Wizard.class));
  }

  @Test
  void testUpdateWizardNotFound() {
    Wizard update = new Wizard();
    update.setName("Wizard updated");
    given(wizardRepository.findById(1)).willReturn(Optional.empty());

    Throwable throwable = catchThrowable(() -> wizardService.update(1, new Wizard()));

    assertThat(throwable).isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find Wizard with Id 1");
    verify(wizardRepository, times(1)).findById(1);
    verify(wizardRepository, times(0)).save(any(Wizard.class));
  }

  @Test
  void testDeleteWizardSuccess() {
    //Given
    Wizard wizard = new Wizard();
    wizard.setId(1);
    wizard.setName("New Wizard");
    given(wizardRepository.findById(1)).willReturn(Optional.of(wizard));
    doNothing().when(wizardRepository).deleteById(1);

    //When
    wizardService.delete(1);
    //then

    verify(wizardRepository, times(1)).deleteById(1);
  }

  @Test
  void testDeleteWizardNotFound() {
    //Given
    Wizard wizard = new Wizard();
    wizard.setName("New Wizard");
    given(wizardRepository.findById(1)).willReturn(Optional.empty());

    //When
    Throwable throwable = catchThrowable(() -> wizardService.delete(1));

    //Then
    assertThat(throwable).isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find Wizard with Id 1");
    verify(wizardRepository, times(1)).findById(1);
    verify(wizardRepository, times(0)).deleteById(1);
  }

  @Test
  void testAssignArtifactSuccess() {
    //Given
    Artifact ar = new Artifact();
    ar.setId("1250808601744904192");
    ar.setName("Invisibility Cloak");
    ar.setDescription("An invisibility Cloak is used to make the wearer invisible");
    ar.setImageUrl("https://www.invisibilitycloak.com");

    Wizard w2 = new Wizard();
    w2.setId(2);
    w2.setName("Harry Potter");
    w2.addArtifact(ar);

    Wizard w3 = new Wizard();
    w3.setId(3 );
    w3.setName("Neville Longbottom");

    given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(ar));
    given(wizardRepository.findById(3)).willReturn(Optional.of(w3));

    //When
    wizardService.assignArtifact(3, "1250808601744904192");

    //Then
    assertThat(ar.getOwner().getId()).isEqualTo(3);
    assertThat(w3.getArtifacts()).contains(ar);
  }

  @Test
  void testAssignArtifactErrorWithNonExistentWizardId() {
    //Given
    Artifact ar = new Artifact();
    ar.setId("1250808601744904192");
    ar.setName("Invisibility Cloak");
    ar.setDescription("An invisibility Cloak is used to make the wearer invisible");
    ar.setImageUrl("https://www.invisibilitycloak.com");

    Wizard w2 = new Wizard();
    w2.setId(2);
    w2.setName("Harry Potter");
    w2.addArtifact(ar);

    given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(ar));
    given(wizardRepository.findById(3)).willReturn(Optional.empty());

    //When
    Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
      wizardService.assignArtifact(3, "1250808601744904192");
    });

    //Then
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
        .hasMessage("Could not find Wizard with Id 3");
    assertThat(ar.getOwner().getId()).isEqualTo(2);
  }

  @Test
  void testAssignArtifactErrorWithNonExistentArtifactId() {
    //Given
    given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());

    //When
    Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
      wizardService.assignArtifact(3, "1250808601744904192");
    });

    //Then
    assertThat(thrown).isInstanceOf(ObjectNotFoundException.class)
      .hasMessage("Could not find Artifact with Id 1250808601744904192");
  }
}