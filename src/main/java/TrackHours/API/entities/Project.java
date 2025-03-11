package TrackHours.API.entities;

import TrackHours.API.DTO.User.UserNoProjectsResponseDTO;
import TrackHours.API.enumTypes.projects.PriorityProject;
import TrackHours.API.enumTypes.projects.StatusProject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projetos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE projetos SET deleted = true WHERE id=?")
@FilterDef(name = "deletedProjectFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedProjectFilter", condition = "deleted = :isDeleted")
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
    @JsonIgnore
    private User responsibleUser;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();

    private boolean deleted = Boolean.FALSE;

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
