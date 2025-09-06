package miu.edu.user.converter;

import miu.edu.dto.UserDto;
import miu.edu.user.HogwartsUser;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<HogwartsUser, UserDto> {

  @Override
  public UserDto convert(HogwartsUser source) {
    return new UserDto(source.getId(), source.getUsername(), source.isEnabled(), source.getRoles());
  }
}
