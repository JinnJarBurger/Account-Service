package account.service;

import account.model.Role;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author adnan
 * @since 12/24/2022
 */
public class CustomRoleSerializer extends StdSerializer<Set<Role>> {

    protected CustomRoleSerializer() {
        this(null);
    }

    protected CustomRoleSerializer(Class<Set<Role>> t) {
        super(t);
    }

    @Override
    public void serialize(Set<Role> roles, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeObject(
                roles.stream()
                        .map(Role::getName)
                        .sorted()
                        .collect(Collectors.toList())
        );
    }
}
