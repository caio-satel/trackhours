package TrackHours.API.repositories;

import TrackHours.API.entities.Project;
import TrackHours.API.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Obter a lista de todos os projetos
    @Query("SELECT p FROM Project p WHERE p.deleted = false")
    List<Project> findAllNotDeleted();

    // Obter um projeto por ID
    @Query("SELECT p FROM Project p WHERE p.id = :id AND p.deleted = false")
    Optional<Project> findByIdAndNotDeleted(@Param("id") Long id);

    // Obter a lista de projetos que o usuário logado é responsável (Exceto deletados)
    @Query("SELECT p FROM Project p WHERE p.responsibleUser = :user AND p.deleted = false")
    List<Project> findByResponsibleUserAndNotDeleted(@Param("user") User user);

    // Obter a lista de total de horas por projeto nos ultimos 30 dias
    @Query("SELECT p.name AS projeto, " +
            "SUM(FUNCTION('TIMESTAMPDIFF', HOUR, r.startTime, r.endTime)) AS totalHoras " +
            "FROM Project p " +
            "JOIN p.tasks t " +
            "JOIN Release r ON r.task = t " +
            "WHERE r.createdAt >= :dataInicio " +
            "AND p.deleted = false " +
            "AND t.deleted = false " +
            "AND r.deleted = false " +
            "GROUP BY p.id " +
            "ORDER BY totalHoras DESC")
    List<Object[]> getTotalHoursByProjectLast30Days(@Param("dataInicio") LocalDateTime dataInicio);

    // Obter lista de tarefas em andamento por projetos
    @Query("SELECT p.name AS projeto, COUNT(t.id) AS ongoingTasks " +
            "FROM Project p " +
            "JOIN p.tasks t " +
            "WHERE t.status NOT IN ('DONE', 'PAUSED') " +
            "AND p.deleted = false " +
            "AND t.deleted = false " +
            "GROUP BY p.id " +
            "ORDER BY ongoingTasks DESC")
    List<Object[]> getOngoingTasksByProject();
}
