package TrackHours.API.repositories;

import TrackHours.API.entities.Task;
import TrackHours.API.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Consulta as tarefas associadas a um usuário específico, que não foram marcadas como excluídas
    @Query("SELECT t FROM Task t JOIN t.integrantes u WHERE u = :user AND t.deleted = false")
    List<Task> findTasksByUser(@Param("user") User user);

    // Consulta as tarefas de um projeto específico em que o usuário logado é integrante, excluindo as que foram marcadas como deletadas
    @Query("SELECT t FROM Task t " +
            "JOIN t.integrantes u " +
            "WHERE t.project.id = :projectId AND u.email = :email AND t.deleted = false")
    List<Task> findTasksByProjectIdAndUserLogged(@Param("projectId") Long projectId, @Param("email") String email);

    // Consulta as tarefas atrasadas de um usuário (não concluídas e com data de término anterior à data atual), exceto as deletadas
    @Query("SELECT t FROM Task t " +
            "JOIN t.integrantes u " +
            "WHERE u.email = :email " +
            "AND t.endDate < CURRENT_DATE " +
            "AND t.status <> 'DONE' " +
            "AND t.deleted = false")
    List<Task> findLateTasksByUser(@Param("email") String email);

    // Consulta todas as tarefas que não foram marcadas como excluídas
    @Query("SELECT t FROM Task t WHERE t.deleted = false")
    List<Task> findAllNotDeleted();

    // Consulta uma tarefa específica pelo ID, desde que não esteja marcada como excluída
    @Query("SELECT t FROM Task t WHERE t.id = :id AND t.deleted = false")
    Optional<Task> findByIdAndNotDeleted(@Param("id") Long id);

    // Consulta todas as tarefas de um projeto específico que não foram marcadas como excluídas
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.deleted = false")
    List<Task> findByProjectIdAndNotDeleted(@Param("projectId") Long projectId);

    // Consulta as tarefas associadas a um usuário específico, desde que não estejam marcadas como excluídas
    @Query("SELECT t FROM Task t JOIN t.integrantes u WHERE u = :user AND t.deleted = false")
    List<Task> findByUserAndNotDeleted(@Param("user") User user);

    // Consulta detalhes de usuários, suas tarefas e projetos, excluindo registros marcados como deletados
    @Query("SELECT u.name AS userName, t.name AS taskName, t.status, p.name AS projectName " +
            "FROM User u " +
            "JOIN u.tasks t " +
            "JOIN t.project p " +
            "WHERE u.deleted = false " +
            "AND t.deleted = false " +
            "AND p.deleted = false")
    List<Object[]> getUserTaskProjectDetails();
}
