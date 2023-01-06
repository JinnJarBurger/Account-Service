package account.controller.api;

import account.model.Event;
import account.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author adnan
 * @since 12/31/2022
 */
@RestController
@RequestMapping("/api/security")
public class SecurityApiController {

    @Autowired
    private EventService eventService;

    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return eventService.findAll();
    }
}
