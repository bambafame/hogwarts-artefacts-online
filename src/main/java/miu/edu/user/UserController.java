package miu.edu.user;

import jakarta.validation.Valid;
import java.util.List;
import miu.edu.dto.UserDto;
import miu.edu.system.Result;
import miu.edu.system.StatusCode;
import miu.edu.user.converter.UserDtoToUserConverter;
import miu.edu.user.converter.UserToUserDtoConverter;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {

  private final UserService userService;
  private final UserDtoToUserConverter userDtoToUserConverter;
  private final UserToUserDtoConverter userToUserDtoConverter;

  public UserController(UserService userService, UserDtoToUserConverter userDtoToUserConverter,
      UserToUserDtoConverter userToUserDtoConverter) {
    this.userService = userService;
    this.userDtoToUserConverter = userDtoToUserConverter;
    this.userToUserDtoConverter = userToUserDtoConverter;
  }

  @GetMapping
  public Result findAllUsers() {
    List<HogwartsUser> foundHogwartsUsers = userService.findAll();
    List<UserDto> userDtos = foundHogwartsUsers.stream().map(userToUserDtoConverter::convert).toList();
    return new Result(true, StatusCode.SUCCESS, "Find All Success", userDtos);
  }

  @GetMapping("/{userId}")
  public Result findUserById(@PathVariable Integer userId) {
    HogwartsUser hogwartsUser = userService.findById(userId);
    return new Result(true, StatusCode.SUCCESS, "Find One Success", userToUserDtoConverter.convert(hogwartsUser));
  }

  @PostMapping
  public Result addUser(@Valid @RequestBody HogwartsUser newHogwartsUser) {
    HogwartsUser hogwartsUser = userService.save(newHogwartsUser);
    UserDto userDto = userToUserDtoConverter.convert(hogwartsUser);
    return new Result(true, StatusCode.SUCCESS, "Add Success", userDto);
  }

  @PutMapping("/{userId}")
  public Result updateUser(@PathVariable Integer userId, @Valid @RequestBody UserDto userDto) {
    HogwartsUser newHogwartsUser = userDtoToUserConverter.convert(userDto);
    HogwartsUser hogwartsUser = userService.update(userId, newHogwartsUser);
    return new Result(true, StatusCode.SUCCESS, "Update Success", userToUserDtoConverter.convert(hogwartsUser));
  }

  @DeleteMapping("/{userId}")
  public Result deleteUser(@PathVariable Integer userId) {
    userService.delete(userId);
    return new Result(true, StatusCode.SUCCESS, "Delete Success");
  }
}
