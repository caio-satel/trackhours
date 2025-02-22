package TrackHours.API.entities;

import TrackHours.API.enumTypes.roles.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "senha", nullable = false)
    private String password;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;

    @Column(name = "ultimo_login")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil")
    private UserRole role;

    @OneToMany(mappedBy = "responsibleUser")
    private List<Project> projects = new ArrayList<>();
}
