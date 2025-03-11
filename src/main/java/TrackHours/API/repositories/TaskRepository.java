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

    @Query("SELECT t FROM Task t JOIN t.integrantes u WHERE u = :user AND t.deleted = false")
    List<Task> findTasksByUser(@Param("user") User user);

    @Query("SELECT t FROM Task t " +
            "JOIN t.integrantes u " +
            "WHERE t.project.id = :projectId AND u.email = :email AND t.deleted = false")
    List<Task> findTasksByProjectIdAndUserLogged(@Param("projectId") Long projectId, @Param("email") String email);

    @Query("SELECT t FROM Task t " +
            "JOIN t.integrantes u " +
            "WHERE u.email = :email " +
            "AND t.endDate < CURRENT_DATE " +
            "AND t.status <> 'DONE' " +
            "AND t.deleted = false")
    List<Task> findLateTasksByUser(@Param("email") String email);

    @Query("SELECT t FROM Task t WHERE t.deleted = false")
    List<Task> findAllNotDeleted();

    @Query("SELECT t FROM Task t WHERE t.id = :id AND t.deleted = false")
    Optional<Task> findByIdAndNotDeleted(@Param("id") Long id);
}
