package TrackHours.API.controllers;

import TrackHours.API.DTO.User.CreateUserDTO;
import TrackHours.API.DTO.User.UpdateUserDTO;
import TrackHours.API.entities.User;
import TrackHours.API.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDTO createUserDTO) {
        var userId = userService.createUser(createUserDTO);
        return ResponseEntity.created(URI.create("/users/" + userId.toString())).build();
    }

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        List<User> listUsers = userService.listUsers();
        return ResponseEntity.ok().body(listUsers);
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
