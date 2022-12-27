package account.service;

import account.model.Event;
import account.model.EventAction;
import account.repository.EventRepository;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author adnan
 * @since 12/27/2022
 */
public class EventLogger implements ApplicationContextAware {

    private static EventRepository eventRepository;
    private static HttpServletRequest request;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        eventRepository = (EventRepository) applicationContext.getBean("eventRepository");
        request = (HttpServletRequest) applicationContext.getBean("httpServletRequest");
    }

    public static void log(EventAction action, Logger logger, LogLevel level, String object) {
        Principal principal = request.getUserPrincipal();

        Event event = Event.builder()
                .date(LocalDateTime.now())
                .action(action)
                .subject(Objects.nonNull(principal) ? principal.getName() : "Anonymous")
                .object(Objects.nonNull(object) ? object : request.getRequestURI())
                .path(request.getRequestURI())
                .build();
        
        event = eventRepository.save(event);

        switch (level) {
            case INFO:
                logger.info("{}", event);
                break;
            case DEBUG:
                logger.debug("{}", event);
                break;
            case WARN:
                logger.warn("{}", event);
                break;
            case ERROR:
                logger.error("{}", event);
                break;
        }
    }
}
