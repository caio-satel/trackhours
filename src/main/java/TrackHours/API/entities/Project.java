package TrackHours.API.entities;

import TrackHours.API.DTO.User.UserNoProjectsResponseDTO;
import TrackHours.API.enumTypes.projects.PriorityProject;
import TrackHours.API.enumTypes.projects.StatusProject;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projetos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "data_inicio", nullable = false)
    private LocalDate startDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "data_fim", nullable = false)
    private LocalDate endDate;

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

    public Project(String name,
                   LocalDate startDate,
                   LocalDate endDate,
                   User responsibleUser,
                   StatusProject status,
                   PriorityProject priority) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.responsibleUser = responsibleUser;
        this.status = status;
        this.priority = priority;
    }

    public Project(Long id,
                   String name,
                   LocalDate localDate,
                   LocalDate localDate1,
                   StatusProject status,
                   PriorityProject priority,
                   UserNoProjectsResponseDTO userNoProjectsResponseDTO) {
    }
}
