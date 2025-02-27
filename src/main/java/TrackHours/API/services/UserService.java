package TrackHours.API.services;

import TrackHours.API.DTO.User.CreateUserDTO;
import TrackHours.API.DTO.User.UpdateRoleUserDTO;
import TrackHours.API.DTO.User.UpdateUserDTO;
import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.roles.UserRole;
import TrackHours.API.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Create User
    public Long createUser(CreateUserDTO createUserDTO) {
        // Cryptographic password
        var hashedPassword = passwordEncoder.encode(createUserDTO.password());
        // DTO -> Entity
        var userCreated = new User(createUserDTO.name(), createUserDTO.email(), hashedPassword, createUserDTO.role());
        var userSaved = userRepository.save(userCreated);

        return userSaved.getId();
    }

    // Find All User
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    // Find By ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Find By User Logged
    public UserDetails findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Update User By ID
    public boolean updateUserById(Long id, UpdateUserDTO updateUserDto) {
        var userEntity = userRepository.findById(id);

        if(userEntity.isPresent()) {
            var user = userEntity.get();

            if (updateUserDto.name() != null && updateUserDto.email() != null && updateUserDto.password() != null) {
                user.setName(updateUserDto.name());
                user.setEmail(updateUserDto.email());

                //Transforma a senha em HASH antes de salvar
                String hashedPassword = passwordEncoder.encode(updateUserDto.password());
                user.setPassword(hashedPassword);

                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    // Update Role User
    public boolean updateRoleUser(Long id, UpdateRoleUserDTO updateRoleUserDTO) {
        var userEntity = userRepository.findById(id);

        if(userEntity.isPresent()) {
            var user = userEntity.get();

            if (updateRoleUserDTO.role() != null) {
                user.setRole(updateRoleUserDTO.role());
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    // Delete By ID
    public boolean deleteById(Long id) {
        var userExists = userRepository.existsById(id);

        if(userExists) {
            userRepository.deleteById(id);
        }

        return userExists;
    }
}
