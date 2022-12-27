package account.repository;

import account.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author adnan
 * @since 12/27/2022
 */
@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
