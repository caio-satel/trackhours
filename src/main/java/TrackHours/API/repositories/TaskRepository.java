package TrackHours.API.repositories;

import TrackHours.API.entities.Task;
import TrackHours.API.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t JOIN t.integrantes u WHERE u = :user")
    List<Task> findTasksByUser(@Param("user") User user);

    @Query("SELECT t FROM Task t " +
            "JOIN t.integrantes u " +
            "WHERE t.project.id = :projectId AND u.email = :email")
    List<Task> findTasksByProjectIdAndUserLogged(@Param("projectId") Long projectId, @Param("email") String email);

    @Query("SELECT t FROM Task t " +
            "JOIN t.integrantes u " +
            "WHERE u.email = :email " +
            "AND t.endDate < CURRENT_DATE " +
            "AND t.status <> 'DONE'")
    List<Task> findLateTasksByUser(@Param("email") String email);
}
