package TrackHours.API.repositories;

import TrackHours.API.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deleted = false")
    UserDetails findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deleted = false")
    Optional<User> findUserByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllNotDeleted();

    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deleted = false")
    Optional<User> findByIdAndNotDeleted(@Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email = :email AND u.deleted = false")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN' AND u.deleted = false")
    List<User> findAllAdminsNotDeleted();

    // Reports
    // Obter o total de horas trabalhadas por usuário
    @Query("SELECT u.name AS usuario, SUM(FUNCTION('TIMESTAMPDIFF', HOUR, r.startTime, r.endTime)) AS totalHoras " +
            "FROM User u " +
            "JOIN Release r ON u.id = r.user.id " + // Relacionamento direto entre User e Release (id_usuario)
            "JOIN r.task t ON t.deleted = false " +
            "WHERE u.deleted = false AND r.deleted = false " +
            "GROUP BY u.id " +
            "HAVING SUM(FUNCTION('TIMESTAMPDIFF', HOUR, r.startTime, r.endTime)) > 0")
    List<Object[]> getTotalHoursByUser();

    // Obter a quantidade total de projetos que o usuário é responsável por usuário
    @Query("SELECT u.name AS usuario, COUNT(p.id) AS totalProjetos " +
            "FROM User u " +
            "JOIN Project p ON u.id = p.responsibleUser.id " +
            "WHERE u.deleted = false AND p.deleted = false " +
            "GROUP BY u.id")
    List<Object[]> getTotalProjectsByUser();

    // Obter o total de tarefas (não concluidas) de cada usuário
    @Query("SELECT u.name AS usuario, COUNT(t.id) AS totalTarefas " +
            "FROM User u " +
            "JOIN u.tasks t " + // Relacionamento entre User e Task
            "JOIN t.project p " + // Relacionamento entre Task e Project
            "WHERE u.deleted = false " +
            "AND t.deleted = false " +
            "AND p.deleted = false " +
            "AND t.status <> 'DONE' " +
            "GROUP BY u.id")
    List<Object[]> getTotalTasksByUser();
}
