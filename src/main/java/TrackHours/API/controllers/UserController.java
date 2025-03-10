package TrackHours.API.controllers;

import TrackHours.API.DTO.User.*;
import TrackHours.API.DTO.mapper.UserMapper;
import TrackHours.API.Exceptions.UsersExceptions.EmailAlreadyExistsException;
import TrackHours.API.Exceptions.UsersExceptions.InvalidPasswordException;
import TrackHours.API.Exceptions.UsersExceptions.UserDatasInvalid;
import TrackHours.API.Exceptions.UsersExceptions.UserNotFoundException;
import TrackHours.API.entities.User;
import TrackHours.API.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "Usuários", description = "Endpoints de usuários")
public class UserController {

    @Autowired
    private UserMapper map;

    @Autowired
    private UserService userService;

    @Operation(summary = "Criar novo usuário (Apenas administradores)")
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserDTO createUserDTO) {
        try {
            User user = userService.createUser(createUserDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (UserDatasInvalid e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Obter lista de usuários (Apenas administradores)")
    @GetMapping
    public ResponseEntity<List<UserNoProjectsResponseDTO>> listUsers() {
        List<User> listUsers = userService.listUsers();

        List<UserNoProjectsResponseDTO> usersResponse = listUsers
                .stream()
                .map(allUsers -> map.userDtoToUser(allUsers))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(usersResponse);
    }

    @Operation(summary = "Obter usuário por ID (Apenas administradores)")
    @GetMapping("/{id}")
    public ResponseEntity<UserNoProjectsResponseDTO> findUserById(@PathVariable Long id) {
        try {
            var user = userService.findUserById(id);
            var response = map.userDtoToUser(user);

            return ResponseEntity.ok(response);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obter ID e Nome do usuário logado")
    @GetMapping("/userLogged")
    public ResponseEntity<UserLoggedDTO> getLoggedUser(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = (User) userService.findByEmail(email);
            UserLoggedDTO response = new UserLoggedDTO(user.getName(), user.getRole());

            return ResponseEntity.ok().body(response);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Atualizar usuário por ID (Apenas administradores)")
    @PutMapping("/{id}")
    public ResponseEntity<Void> uptadeUserById(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {
        boolean userUpdated = userService.updateUserById(id, updateUserDTO);

        if (!userUpdated) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Atualizar role de usuário por ID (Apenas administradores)")
    @PutMapping("/role/{id}")
    public ResponseEntity<Void> updateRoleUser(@PathVariable Long id, @RequestBody UpdateRoleUserDTO updateRoleUserDTO) {
        boolean userUpdated = userService.updateRoleUser(id, updateRoleUserDTO);

        if (!userUpdated) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Deletar usuário por ID (Apenas administradores)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean userExists = userService.deleteById(id);

        if (!userExists) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mudar a senha do usuário logado")
    @PatchMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, Authentication authentication) {
        try {
            boolean passwordChanged = userService.changePassword(authentication, changePasswordDTO);

            if (passwordChanged) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().body("Falha ao alterar a senha");
            }
        } catch (InvalidPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
