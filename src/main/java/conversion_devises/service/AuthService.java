package conversion_devises.service;

import conversion_devises.dto.response.AuthReponse;
import conversion_devises.dto.request.LoginRequest;
import conversion_devises.dto.request.RegisterRequest;
import conversion_devises.entity.Role;
import conversion_devises.entity.User;
import conversion_devises.repository.UserRepository;
import conversion_devises.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthReponse register(RegisterRequest request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already used");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.USER)
                .build();
        userRepository.save(user);
        String token  = jwtUtil.generateToken(user);
        return  AuthReponse.builder()
                .token(token)
                .username(user.getUsername())
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
    public AuthReponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        String token = jwtUtil.generateToken(user);
        return  AuthReponse.builder()
                .token(token)
                .username(user.getUsername())
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
