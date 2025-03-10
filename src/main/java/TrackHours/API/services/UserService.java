package TrackHours.API.services;

import TrackHours.API.DTO.User.ChangePasswordDTO;
import TrackHours.API.DTO.User.CreateUserDTO;
import TrackHours.API.DTO.User.UpdateRoleUserDTO;
import TrackHours.API.DTO.User.UpdateUserDTO;
import TrackHours.API.Exceptions.UsersExceptions.EmailAlreadyExistsException;
import TrackHours.API.Exceptions.UsersExceptions.InvalidPasswordException;
import TrackHours.API.Exceptions.UsersExceptions.UserDatasInvalid;
import TrackHours.API.Exceptions.UsersExceptions.UserNotFoundException;
import TrackHours.API.entities.User;
import TrackHours.API.enumTypes.roles.UserRole;
import TrackHours.API.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    public User createUser(CreateUserDTO createUserDTO) {
        if (createUserDTO == null
                || createUserDTO.name() == null
                || createUserDTO.email() == null
                || createUserDTO.password() == null
                || createUserDTO.role() == null) {
            throw new UserDatasInvalid("Dados do usuário inválidos");
        }

        if (userRepository.existsByEmail(createUserDTO.email())) {
            throw new EmailAlreadyExistsException("E-mail já cadastrado");
        }

        var hashedPassword = passwordEncoder.encode(createUserDTO.password());
        var dtoToUser = new User(createUserDTO.name(), createUserDTO.email(), hashedPassword, createUserDTO.role());

        return userRepository.save(dtoToUser);
    }

    // Find All User
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    // Find By ID
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
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

    // Change password user logged
    public boolean changePassword(Authentication authentication, ChangePasswordDTO changePasswordDTO) {
        String email = authentication.getName();

        var userLogged = userRepository.findUserByEmail(email);

        if (userLogged.isPresent()) {
            var user = userLogged.get();

            if (!passwordEncoder.matches(changePasswordDTO.currentPassword(), user.getPassword())) {
                throw new InvalidPasswordException("Senha atual incorreta");
            }

            if (changePasswordDTO.newPassword().length() < 6) {
                throw new InvalidPasswordException("A nova senha deve ter pelo menos 6 caracteres");
            }

            String hashedPassword = passwordEncoder.encode(changePasswordDTO.newPassword());
            user.setPassword(hashedPassword);
            userRepository.save(user);

            return true;
        }

        throw new UserNotFoundException("Usuário não encontrado");
    }
}
