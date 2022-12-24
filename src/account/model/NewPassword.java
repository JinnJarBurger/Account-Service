package account.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * @author adnan
 * @since 11/30/2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewPassword {

    @Length(min = 12, message = "Password length must be 12 chars minimum!")
    @JsonAlias("new_password")
    private String password;
}
