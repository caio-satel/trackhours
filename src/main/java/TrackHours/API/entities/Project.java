package TrackHours.API.entities;

import TrackHours.API.enumTypes.projects.PriorityProject;
import TrackHours.API.enumTypes.projects.StatusProject;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projetos")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    // Planned, Progress, Done or Canceled
    @Enumerated(EnumType.STRING)
    private StatusProject status;

    // High, Medium or Low
    @Enumerated(EnumType.STRING)
    private PriorityProject priority;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm") // Retorno da data será nesse formato JSON
    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User responsibleUser;

    @OneToMany(mappedBy = "projectId")
    private List<Task> tasks = new ArrayList<>();
}
