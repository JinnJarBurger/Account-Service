package account.security;

import account.model.User;
import account.service.EventLogger;
import account.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static account.model.EventAction.*;
import static org.springframework.boot.logging.LogLevel.*;

/**
 * @author adnan
 * @since 12/30/2022
 */
@Component
public class RestAuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestAuthenticationFailureListener.class);

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {

        Authentication authentication = event.getAuthentication();
        String email = authentication.getName();
        User user = userService.findByEmail(email);

        if (user.isEnabled() && user.isAccountNonLocked()) {
            if (user.getFailedAttempt() < UserService.MAX_FAILED_ATTEMPTS - 1) {
                userService.increaseFailedAttempts(user);

                EventLogger.log(LOGIN_FAILED, LOGGER, WARN, request, null);
            } else {
                userService.lock(user);

                EventLogger.log(BRUTE_FORCE, LOGGER, FATAL, request, null);
                EventLogger.log(LOCK_USER, LOGGER, FATAL, request, "Lock user " + email);

                throw new LockedException("Your account has been locked due to 3 failed attempts. It will be unlocked after 24 hours.");
            }
        } else if (!user.isAccountNonLocked()) {
            if (userService.unlockWhenTimeExpired(user)) {
                EventLogger.log(UNLOCK_USER, LOGGER, INFO, request, "Unlock user " + email);

                throw new LockedException("Your account has been unlocked. Please try to login again.");
            }
        }
    }
}
