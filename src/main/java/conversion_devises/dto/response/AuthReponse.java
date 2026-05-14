package conversion_devises.dto.response;

import conversion_devises.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthReponse {
private String token;
private Long id;
private String username;
private String email;
private Role role;
}
