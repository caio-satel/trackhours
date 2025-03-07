package TrackHours.API.controllers;

import TrackHours.API.DTO.User.AuthenticationDTO;
import TrackHours.API.DTO.User.LoginResponseDTO;
import TrackHours.API.DTO.User.RegisterDTO;
import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.roles.UserRole;
import TrackHours.API.repositories.UserRepository;
import TrackHours.API.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Validated AuthenticationDTO data){
        var user = userRepository.findByEmail(data.email());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado");
        }

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        // Obtem usuário autenticado
        var userAuthenticate = (User) auth.getPrincipal();

        // Atualiza o lastLogin com UTC correto no banco
        userAuthenticate.setLastLogin(Instant.now().plus(Duration.ofHours(-3)));
        userRepository.save(userAuthenticate);

        // Gerar o token
        var token = tokenService.generateToken(userAuthenticate);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody @Validated RegisterDTO data) {
        if (this.userRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.name(), data.email(), encryptedPassword, UserRole.ADMIN);

        this.userRepository.save(newUser);

        return ResponseEntity.ok(Map.of("message", "Usuário criado com sucesso!"));
    }
}
