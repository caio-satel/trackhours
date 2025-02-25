package TrackHours.API.controllers;

import TrackHours.API.DTO.User.CreateUserDTO;
import TrackHours.API.DTO.User.UpdateUserDTO;
import TrackHours.API.DTO.User.UserNoProjectsResponseDTO;
import TrackHours.API.DTO.mapper.UserMapper;
import TrackHours.API.entities.User;
import TrackHours.API.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserMapper map;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDTO createUserDTO) {
        var userId = userService.createUser(createUserDTO);
        return ResponseEntity.created(URI.create("/users/" + userId.toString())).build();
    }

    @GetMapping
    public ResponseEntity<List<UserNoProjectsResponseDTO>> listUsers() {
        List<User> listUsers = userService.listUsers();

        List<UserNoProjectsResponseDTO> usersResponse = listUsers
                .stream()
                .map(allUsers -> map.userDtoToUser(allUsers))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(usersResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        var userById = userService.findUserById(id);

        if (userById.isPresent()) {
            return ResponseEntity.ok(userById.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/userLogged")
    public ResponseEntity<Object> getLoggedUser(Authentication authentication) {
        String email = authentication.getName(); // Email do usuário autenticado
        User user = (User) userService.findByEmail(email); // Busca no banco pelo email

        String nameUserLogged = user.getName();

        return ResponseEntity.ok().body(nameUserLogged);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> uptadeUserById(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {
        boolean userUpdated = userService.updateUserById(id, updateUserDTO);

        if (!userUpdated) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean userExists = userService.deleteById(id);

        if (!userExists) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
