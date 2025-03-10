package TrackHours.API.repositories;

import TrackHours.API.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DashboardRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p " +
            "JOIN p.tasks t " +
            "JOIN t.integrantes u " +
            "WHERE u.email = :email")
    List<Project> findProjectsAndTasksByUser(@Param("email") String email);

    @Query("SELECT COUNT(t) FROM Task t " +
            "JOIN t.integrantes u " +
            "WHERE u.email = :email AND t.endDate < CURRENT_DATE")
    Long countLateTasksByUser(@Param("email") String email);

}
