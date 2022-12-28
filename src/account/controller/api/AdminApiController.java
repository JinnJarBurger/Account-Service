package account.controller.api;

import account.model.EventAction;
import account.model.Operation;
import account.model.RoleChangeDto;
import account.model.User;
import account.service.EventLogger;
import account.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import static account.model.EventAction.DELETE_USER;
import static account.model.Operation.GRANT;
import static org.springframework.boot.logging.LogLevel.INFO;

/**
 * @author adnan
 * @since 12/24/2022
 */
@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminApiController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @DeleteMapping("/user/{email}")
    public Map<String, String> deleteUser(@PathVariable String email) {
        userService.delete(email);

        EventLogger.log(DELETE_USER, LOGGER, INFO, request, email);

        return Map.of(
                "user", email,
                "status", "Deleted successfully!"
        );
    }

    @PutMapping("/user/role")
    public User changeRole(@Valid @RequestBody RoleChangeDto roleChangeDto) {
        User user = userService.changeRole(roleChangeDto);
        Operation operation = roleChangeDto.getOperation();

        EventLogger.log(EventAction.valueOf(operation.name() + "_ROLE"),
                LOGGER, INFO, request,
                (operation == GRANT ? "Grant" : "Remove") +
                        " role " + roleChangeDto.getRole() +
                        (operation == GRANT ? " to " : " from ")
                        + user.getEmail());

        return user;
    }
}
