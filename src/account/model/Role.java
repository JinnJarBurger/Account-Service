package account.model;

import account.repository.RoleRepository;
import lombok.*;

import javax.persistence.*;

/**
 * @author adnan
 * @since 12/24/2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "role")
public class Role {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_ADMINISTRATOR = "ROLE_ADMINISTRATOR";
    public static final String ROLE_ACCOUNTANT = "ROLE_ACCOUNTANT";
    public static final String ROLE_USER = "ROLE_USER";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    public Role(String name) {
        this.name = name;
    }
}
