package account.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author adnan
 * @since 12/24/2022
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleChangeDto {

    @NotBlank
    private String user;

    @NotBlank
    private String role;

    @NotNull
    private Operation operation;
}
