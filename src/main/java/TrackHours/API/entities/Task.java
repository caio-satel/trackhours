package TrackHours.API.entities;

import TrackHours.API.enumTypes.tasks.StatusTask;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
@Table(name = "tarefas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE tarefas SET deleted = true WHERE id=?")
@FilterDef(name = "deletedTaskFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedTaskFilter", condition = "deleted = :isDeleted")
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
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @Column(name = "data_fim", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    // Open, Progress, Done or Paused
    @Enumerated(EnumType.STRING)
    private StatusTask status;

    @ManyToOne
    @JoinColumn(name = "id_projeto", nullable = false)
    private Project project;

    @ManyToMany(mappedBy = "tasks")
    private List<User> integrantes = new ArrayList<>();

    private boolean deleted = Boolean.FALSE;

    public Task(String name,
                LocalDate startDate,
                LocalDate endDate,
                Project project,
                StatusTask status) {

        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = project;
        this.status = status;
    }

    public void setIntegrantes(List<User> users) {
        this.integrantes = users;
        // Atualiza o outro lado do relacionamento
        for (User user : users) {
            if (!user.getTasks().contains(this)) {
                user.getTasks().add(this);
            }
        }
    }

    public void addCollaborator(User user) {
        this.integrantes.add(user);
        user.getTasks().add(this);
    }

    public void removeCollaborator(User user) {
        this.integrantes.remove(user);
        user.getTasks().remove(this);
    }
}
