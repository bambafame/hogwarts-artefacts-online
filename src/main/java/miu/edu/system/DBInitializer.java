package miu.edu.system;

import miu.edu.artifact.Artifact;
import miu.edu.user.HogwartsUser;
import miu.edu.user.UserRepository;
import miu.edu.wizard.Wizard;
import miu.edu.wizard.WizardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer implements CommandLineRunner {

  @Autowired
  private WizardRepository wizardRepository;
  @Autowired
  private UserRepository userRepository;

  @Override
  public void run(String... args) throws Exception {
    //Users
    HogwartsUser u1 = new HogwartsUser();
    u1.setPassword("123456");
    u1.setUsername("eric");
    u1.setEnabled(true);
    u1.setRoles("User");

    HogwartsUser u2 = new HogwartsUser();
    u2.setPassword("password");
    u2.setUsername("cheikh");
    u2.setEnabled(true);
    u2.setRoles("Admin User");

    HogwartsUser u3 = new HogwartsUser();
    u3.setPassword("qwerty");
    u3.setUsername("tom");
    u3.setEnabled(true);
    u3.setRoles("User");

    userRepository.save(u1);
    userRepository.save(u2);
    userRepository.save(u3);

    // --- Wizards ---
    Wizard merlin = new Wizard();
    merlin.setName("Merlin the Wise");

    Wizard morgana = new Wizard();
    morgana.setName("Morgana the Enchantress");

    Wizard alatar = new Wizard();
    alatar.setName("Alatar the Wanderer");

    // --- Artifacts ---
    Artifact staff = new Artifact();
    staff.setId("A1");
    staff.setName("Staff of Eternity");
    staff.setDescription("Emits endless magical energy.");
    staff.setImageUrl("images/staff.png");
    merlin.addArtifact(staff);

    Artifact orb = new Artifact();
    orb.setId("A2");
    orb.setName("Crystal Orb of Time");
    orb.setDescription("Shows glimpses of the past and future.");
    orb.setImageUrl("images/orb.png");
    merlin.addArtifact(orb);

    Artifact dagger = new Artifact();
    dagger.setId("A3");
    dagger.setName("Shadow Dagger");
    dagger.setDescription("Infused with dark energy.");
    dagger.setImageUrl("images/dagger.png");
    morgana.addArtifact(dagger);

    Artifact cloak = new Artifact();
    cloak.setId("A4");
    cloak.setName("Cloak of Illusions");
    cloak.setDescription("Renders the wearer invisible.");
    cloak.setImageUrl("images/cloak.png");
    morgana.addArtifact(cloak);

    Artifact ring = new Artifact();
    ring.setId("A5");
    ring.setName("Emerald Ring of Healing");
    ring.setDescription("Restores vitality and heals wounds.");
    ring.setImageUrl("images/ring.png");
    alatar.addArtifact(ring);

    Artifact amulet = new Artifact();
    amulet.setId("A6");
    amulet.setName("Phoenix Feather Amulet");
    amulet.setDescription("Protects against death once.");
    amulet.setImageUrl("images/amulet.png");
    alatar.addArtifact(amulet);


    wizardRepository.save(merlin);
    wizardRepository.save(morgana);
    wizardRepository.save(alatar);
  }
}
