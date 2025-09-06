package miu.edu.user;

import jakarta.validation.Valid;
import java.util.List;
import miu.edu.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<HogwartsUser> findAll() {
    return userRepository.findAll();
  }

  public HogwartsUser findById(Integer userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new ObjectNotFoundException("User", String.valueOf(userId)));
  }

  public HogwartsUser save(@Valid HogwartsUser newHogwartsUser) {
    return userRepository.save(newHogwartsUser);
  }

  public HogwartsUser update(Integer userId, HogwartsUser newHogwartsUser) {
    HogwartsUser oldHogwartsUser = findById(userId);
    oldHogwartsUser.setUsername(newHogwartsUser.getUsername());
    oldHogwartsUser.setEnabled(newHogwartsUser.isEnabled());
    oldHogwartsUser.setRoles(newHogwartsUser.getRoles());
    return userRepository.save(oldHogwartsUser);
  }

  public void delete(Integer userId) {
    HogwartsUser oldHogwartsUser = findById(userId);
    userRepository.deleteById(userId);
  }
}
