package account.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

import static account.model.Role.ROLE_ADMINISTRATOR;

/**
 * @author adnan
 * @since 11/16/2022
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String lastname;

    @NotBlank
    @Email(regexp = ".*@acme.com")
    private String email;

    @NotBlank
    @Length(min = 12, message = "The password length must be at least 12 chars!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User() {
        roles = new HashSet<>();
    }

    @JsonIgnore
    public boolean isNew() {
        return getId() == null || getId() == 0;
    }

    @JsonIgnore
    public boolean isAdmin() {
        return !getRoles().isEmpty() && getRoles().stream().anyMatch(role -> role.getName().equals(ROLE_ADMINISTRATOR));
    }
}
