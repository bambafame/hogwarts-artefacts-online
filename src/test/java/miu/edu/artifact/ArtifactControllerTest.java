package miu.edu.artifact;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import miu.edu.dto.ArtifactDto;
import miu.edu.exception.ObjectNotFoundException;
import miu.edu.system.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ArtifactController.class)
@AutoConfigureMockMvc
class ArtifactControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockitoBean
  private ArtifactService artifactService;

  @Autowired
  private ObjectMapper objectMapper;

  @Value("${api.endpoint.base-url}")
  String baseUrl;

  List<Artifact> artifacts;

  @BeforeEach
  void setUp() {
    artifacts = new ArrayList<Artifact>();
    Artifact a1 = new Artifact();
    a1.setId("1");
    a1.setName("Ancient Vase");
    a1.setDescription("A beautifully preserved vase from the Ming dynasty.");
    a1.setImageUrl("https://example.com/images/vase.jpg");
    artifacts.add(a1);

    Artifact a2 = new Artifact();
    a2.setId("2");
    a2.setName("Pharaoh’s Mask");
    a2.setDescription("A golden funerary mask used in ancient Egypt.");
    a2.setImageUrl("https://example.com/images/mask.jpg");
    artifacts.add(a2);

    Artifact a3 = new Artifact();
    a3.setId("3");
    a3.setName("Roman Coin");
    a3.setDescription("A silver coin from the Roman Empire, dated 100 AD.");
    a3.setImageUrl("https://example.com/images/coin.jpg");
    artifacts.add(a3);

    Artifact a4 = new Artifact();
    a4.setId("4");
    a4.setName("Samurai Sword");
    a4.setDescription("A traditional katana used by Japanese samurai warriors.");
    a4.setImageUrl("https://example.com/images/sword.jpg");
    artifacts.add(a4);

    Artifact a5 = new Artifact();
    a5.setId("5");
    a5.setName("Medieval Shield");
    a5.setDescription("A knight’s shield with a family crest, 14th century.");
    a5.setImageUrl("https://example.com/images/shield.jpg");
    artifacts.add(a5);

    Artifact a6 = new Artifact();
    a6.setId("6");
    a6.setName("Native American Pottery");
    a6.setDescription("Handcrafted clay pottery from the Pueblo peoples.");
    a6.setImageUrl("https://example.com/images/pottery.jpg");
    artifacts.add(a6);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void tesFindArtifactByIdSuccess() throws Exception {
    //Given
    given(this.artifactService.findById("1")).willReturn(artifacts.get(0));
    //When and Then
    mockMvc.perform(get(baseUrl+"/artifacts/1").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
        .andExpect(jsonPath("$.message").value("Find One Success"))
        .andExpect(jsonPath("$.data.id").value("1"))
        .andExpect(jsonPath("$.data.name").value("Ancient Vase"));
    verify(artifactService, times(1)).findById("1");

  }

  @Test
  void tesFindArtifactByIdError() throws Exception {
    //Given
    given(this.artifactService.findById("1")).willThrow(new ObjectNotFoundException("Artifact", "1"));
    //When and Then
    mockMvc.perform(get(baseUrl+"/artifacts/1").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(false))
        .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
        .andExpect(jsonPath("$.message").value("Could not find Artifact with Id 1"))
        .andExpect(jsonPath("$.data").isEmpty());
    verify(artifactService, times(1)).findById("1");

  }

  @Test
  void testFindAllArtifactsSuccess() throws Exception {
    given(this.artifactService.findAll()).willReturn(artifacts);

    mockMvc.perform(get(baseUrl+"/artifacts").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
        .andExpect(jsonPath("$.message").value("Find All Success"))
        .andExpect(jsonPath("$.data", Matchers.hasSize(artifacts.size())))
        .andExpect(jsonPath("$.data[0].id").value("1"))
        .andExpect(jsonPath("$.data[0].name").value("Ancient Vase"));
  }

  @Test
  void testAddArtifactSuccess() throws Exception {
    //Given
    ArtifactDto artifactDto = new ArtifactDto(null, "artifact 3", "description...", "img Url", null);
    String json = objectMapper.writeValueAsString(artifactDto);
    Artifact artifact = new Artifact();
    artifact.setId("123456");
    artifact.setName("artifact 3");
    artifact.setDescription("description...");
    artifact.setImageUrl("img Url");
    given(artifactService.save(Mockito.any(Artifact.class))).willReturn(artifact);

    //When and Then
    mockMvc.perform(post(baseUrl+"/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
        .andExpect(jsonPath("$.message").value("Add Success"))
        .andExpect(jsonPath("$.data.id").isNotEmpty())
        .andExpect(jsonPath("$.data.name").value("artifact 3"));
  }

  @Test
  void testUpdateArtifactSuccess() throws Exception {
    //Given
    ArtifactDto artifactDto = new ArtifactDto("123456", "artifact 3", "description...", "img Url", null);
    String json = objectMapper.writeValueAsString(artifactDto);
    Artifact updatedArtifact = new Artifact();
    updatedArtifact.setId("123456");
    updatedArtifact.setName("artifact 3");
    updatedArtifact.setDescription("description...");
    updatedArtifact.setImageUrl("img Url");
    given(artifactService.update(eq("123456"), Mockito.any(Artifact.class))).willReturn(updatedArtifact);

    //When and Then
    mockMvc.perform(put(baseUrl+"/artifacts/123456").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
        .andExpect(jsonPath("$.message").value("Update Success"))
        .andExpect(jsonPath("$.data.id").value("123456"))
        .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()));
  }

  @Test
  void testUpdateArtifactErrorWithNoExistingId() throws Exception {
//Given
    ArtifactDto artifactDto = new ArtifactDto("123456789", "artifact 3", "description...", "img Url", null);
    String json = objectMapper.writeValueAsString(artifactDto);

    given(artifactService.update(eq("123456789"), Mockito.any(Artifact.class))).willThrow(new ObjectNotFoundException("Artifact", "123456789"));

    //When and Then
    mockMvc.perform(put(baseUrl+"/artifacts/123456789").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(false ))
        .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
        .andExpect(jsonPath("$.message").value("Could not find Artifact with Id 123456789"))
        .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testDeleteArtifactSuccess() throws Exception {
    //Given
    doNothing().when(artifactService).delete("123456");

    //When and Then
    mockMvc.perform(delete(baseUrl+"/artifacts/123456").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(true ))
        .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
        .andExpect(jsonPath("$.message").value("Delete Success"))
        .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testDeleteArtifactErrorWithNoExistingId() throws Exception {
    //Given
    doThrow(new ObjectNotFoundException("Artifact", "123456789"))
        .when(artifactService).delete("123456789");

    //When and Then
    mockMvc.perform(delete(baseUrl+"/artifacts/123456789").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(false ))
        .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
        .andExpect(jsonPath("$.message").value("Could not find Artifact with Id 123456789"))
        .andExpect(jsonPath("$.data").doesNotExist());
  }
}