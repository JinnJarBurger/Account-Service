package account.service;

import account.model.Role;
import account.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static account.model.Role.*;

/**
 * @author adnan
 * @since 12/24/2022
 */
@Component
public class DataLoader {

    @Autowired
    private RoleRepository roleRepository;

    public DataLoader() {
        createRoles();
    }

    private void createRoles() {
        try {
            roleRepository.save(new Role(ROLE_ADMINISTRATOR));
            roleRepository.save(new Role(ROLE_USER));
            roleRepository.save(new Role(ROLE_ACCOUNTANT));
        } catch (Exception ignored) {

        }
    }
}
