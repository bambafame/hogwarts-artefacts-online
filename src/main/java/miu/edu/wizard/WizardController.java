package miu.edu.wizard;

import jakarta.validation.Valid;
import java.util.List;
import miu.edu.dto.WizardDto;
import miu.edu.system.Result;
import miu.edu.system.StatusCode;
import miu.edu.wizard.converter.WizardDtoToWizardConverter;
import miu.edu.wizard.converter.WizardToWizardDtoConverter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {

  private final WizardService wizardService;
  private final WizardToWizardDtoConverter wizardToWizardDtoConverter;
  private final WizardDtoToWizardConverter wizardDtoToWizardConverter;

  public WizardController(WizardService wizardService,
      WizardToWizardDtoConverter wizardToWizardDtoConverter,
      WizardDtoToWizardConverter wizardDtoToWizardConverter) {
    this.wizardService = wizardService;
    this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
  }

  @GetMapping("/{wizardId}")
  public Result findWizardById(@PathVariable Integer wizardId) {
    Wizard wizard = wizardService.findById(wizardId);
    return new Result(true, StatusCode.SUCCESS, "Find One Success", wizardToWizardDtoConverter.convert(wizard));
  }

  @GetMapping
  public Result findAllWizards() {
    List<Wizard> wizards = wizardService.findAll();
    List<WizardDto> wizardDtos = wizards.stream().map(wizardToWizardDtoConverter::convert).toList();
    return new Result(true, StatusCode.SUCCESS, "Find All Success", wizardDtos);
  }

  @PostMapping
  public Result addWizard(@Valid @RequestBody WizardDto wizardDto) {
    Wizard wizard = wizardDtoToWizardConverter.convert(wizardDto);
    wizard = wizardService.save(wizard);
    return new Result(true, StatusCode.SUCCESS, "Add Success", wizardToWizardDtoConverter.convert(wizard));
  }

  @PutMapping("/{wizardId}")
  public Result updateWizard(@PathVariable Integer wizardId, @Valid @RequestBody WizardDto wizardDto) {
    Wizard wizard = wizardDtoToWizardConverter.convert(wizardDto);
    Wizard updatedWizard = wizardService.update(wizardId, wizard);
    return new Result(true, StatusCode.SUCCESS, "Update Success", wizardToWizardDtoConverter.convert(updatedWizard));
  }

  @DeleteMapping("/{wizardId}")
  public Result deleteWizard(@PathVariable Integer wizardId) {
    wizardService.delete(wizardId);
    return new Result(true, StatusCode.SUCCESS, "Delete Success");
  }

}
