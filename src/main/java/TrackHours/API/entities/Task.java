package TrackHours.API.entities;

import TrackHours.API.enumTypes.tasks.StatusTask;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tarefas")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "data_de_criacao", nullable = false, updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;

    @Column(name = "data_inicio", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime startDate;

    @Column(name = "data_fim", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime endDate;

    // Open, Progress, Done or Paused
    @Enumerated(EnumType.STRING)
    private StatusTask status;

    @ManyToOne
    @JoinColumn(name = "id_projeto", nullable = false)
    private Project projectId;

    @ManyToMany
    @JoinTable(
            name = "usuario_tarefas",
            joinColumns = @JoinColumn(name = "id_tarefas"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<User> collaborators = new ArrayList<>();
}
