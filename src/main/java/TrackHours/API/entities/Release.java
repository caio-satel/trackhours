package TrackHours.API.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "lançamentos")
public class Release {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(name = "data_inicio", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyy HH:mm")
    private LocalDateTime startDate;

    @Column(name = "data_fim", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyy HH:mm")
    private LocalDateTime endDate;

    @Column(name = "data_registro", nullable = false, updatable = false)
    @CreationTimestamp
    @JsonFormat(pattern = "dd/MM/yyy HH:mm")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_atividade", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private User user;
}
