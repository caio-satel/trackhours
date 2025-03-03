package TrackHours.API.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "lançamentos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "data_lançamento", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateRelease;

    @Column(name = "horario_inicio", nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @Column(name = "horario_fim", nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Column(name = "data_registro", nullable = false, updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_atividade", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;

    public Release(String description,
                   LocalDate dateRelease,
                   LocalTime startTime,
                   LocalTime endTime,
                   Task task,
                   User user) {
        this.description = description;
        this.dateRelease = dateRelease;
        this.startTime = startTime;
        this.endTime = endTime;
        this.task = task;
        this.user = user;
    }
}
