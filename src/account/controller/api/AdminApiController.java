package account.controller.api;

import account.model.RoleChangeDto;
import account.model.User;
import account.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author adnan
 * @since 12/24/2022
 */
@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    UserService userService;

    public AdminApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @DeleteMapping("/user/{email}")
    public Map<String, String> deleteUser(@PathVariable String email) {
        userService.delete(email);

        return Map.of(
                "user", email,
                "status", "Deleted successfully!"
        );
    }

    @PutMapping("/user/role")
    public User changeRole(@Valid @RequestBody RoleChangeDto roleChangeDto) {
        return userService.changeRole(roleChangeDto);
    }
}
