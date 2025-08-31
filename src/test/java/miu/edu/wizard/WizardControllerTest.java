package miu.edu.wizard;

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
import miu.edu.dto.WizardDto;
import miu.edu.exception.ObjectNotFoundException;
import miu.edu.system.StatusCode;
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

@WebMvcTest(controllers = WizardController.class)
@AutoConfigureMockMvc
class WizardControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private WizardService wizardService;

  @Autowired
  private ObjectMapper objectMapper;

  @Value("${api.endpoint.base-url}")
  String baseUrl;

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
  void testFindWizardByIdSuccess() throws Exception {
    //GIVEN
    given(wizardService.findById(1)).willReturn(wizards.get(0));
    //When and then
    mockMvc.perform(get(baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
        .andExpect(jsonPath("$.message").value("Find One Success"))
        .andExpect(jsonPath("$.data.id").value("1"))
        .andExpect(jsonPath("$.data.name").value("New Wizard 1"));
    verify(wizardService, times(1)).findById(1);
  }

  @Test
  void testFindWizardByIdError() throws Exception {
    //GIVEN
    given(wizardService.findById(1)).willThrow(new ObjectNotFoundException("Wizard" ,"1"));
    //When and then
    mockMvc.perform(get(baseUrl+"/wizards/1").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(false))
        .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
        .andExpect(jsonPath("$.message").value("Could not find Wizard with Id 1"))
        .andExpect(jsonPath("$.data").isEmpty());
    verify(wizardService, times(1)).findById(1);
  }

  @Test
  void testFindAllWizards() throws Exception {
    given(wizardService.findAll()).willReturn(wizards);
    mockMvc.perform(get(baseUrl+"/wizards").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
        .andExpect(jsonPath("$.message").value("Find All Success"))
        .andExpect(jsonPath("$.data[0].id").value(1))
        .andExpect(jsonPath("$.data[0].name").value("New Wizard 1"));
  }

  @Test
  void testAddWizardSuccess() throws Exception {
    //Given
    WizardDto wizardDto = new WizardDto(null, "New Wizard 4", 0);
    String json = objectMapper.writeValueAsString(wizardDto);
    Wizard wizard = new Wizard();
    wizard.setId(4);
    wizard.setName("New Wizard 4");
    given(wizardService.save(Mockito.any(Wizard.class))).willReturn(wizard);

    //When and Then
    mockMvc.perform(post(baseUrl+"/wizards").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
        .andExpect(jsonPath("$.message").value("Add Success"))
        .andExpect(jsonPath("$.data.id").isNotEmpty())
        .andExpect(jsonPath("$.data.name").value("New Wizard 4"));
  }

  @Test
  void testUpdateWizardSuccess() throws Exception {
    WizardDto wizardDto = new WizardDto(1, "New Wizard updated", 0);
    String json = objectMapper.writeValueAsString(wizardDto);
    Wizard wizard = new Wizard();
    wizard.setId(1);
    wizard.setName("New Wizard updated");
    given(wizardService.update(eq(1), Mockito.any(Wizard.class))).willReturn(wizard);

    mockMvc.perform(put(baseUrl+"/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
        .andExpect(jsonPath("$.message").value("Update Success"))
        .andExpect(jsonPath("$.data.id").value(1))
        .andExpect(jsonPath("$.data.name").value(wizard.getName()));
  }

  @Test
  void testUpdateWizardError() throws Exception {
    WizardDto wizardDto = new WizardDto(1, "New Wizard updated", 0);
    String json = objectMapper.writeValueAsString(wizardDto);
    given(wizardService.update(eq(1), Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("Wizard", "1"));

    mockMvc.perform(put(baseUrl+"/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(false))
        .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
        .andExpect(jsonPath("$.message").value("Could not find Wizard with Id 1"))
        .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testDeleteWizardSuccess() throws Exception {
    Wizard wizard = new Wizard();
    wizard.setId(1);
    wizard.setName("New Wizard updated");
    doNothing().when(wizardService).delete(eq(1));

    mockMvc.perform(delete(baseUrl+"/wizards/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(true))
        .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
        .andExpect(jsonPath("$.message").value("Delete Success"))
        .andExpect(jsonPath("$.data").doesNotExist());
  }

  @Test
  void testDeleteWizardError() throws Exception {
    doThrow(new ObjectNotFoundException("Wizard", "1")).when(wizardService).delete(eq(1));

    mockMvc.perform(delete(baseUrl+"/wizards/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.flag").value(false))
        .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
        .andExpect(jsonPath("$.message").value("Could not find Wizard with Id 1"))
        .andExpect(jsonPath("$.data").doesNotExist());
  }
}