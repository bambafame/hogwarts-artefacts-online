package miu.edu.user.converter;

import miu.edu.dto.UserDto;
import miu.edu.user.HogwartsUser;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, HogwartsUser> {

  @Override
  public HogwartsUser convert(UserDto source) {
    HogwartsUser hogwartsUser = new HogwartsUser();
    BeanUtils.copyProperties(source, hogwartsUser);
    return hogwartsUser;
  }
}
