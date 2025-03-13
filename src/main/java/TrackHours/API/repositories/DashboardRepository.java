package TrackHours.API.repositories;

import TrackHours.API.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DashboardRepository extends JpaRepository<Project, Long> {

    // Consulta projetos distintos que possuem tarefas pendentes (não concluídas) atribuídas a um usuário logado pelo e-mail,
    // excluindo as tarefas que foram marcadas como deletadas
    @Query("SELECT DISTINCT p FROM Project p " +
            "JOIN p.tasks t " +
            "JOIN t.integrantes u " +
            "WHERE u.email = :email AND t.status <> 'DONE' AND t.deleted = false")
    List<Project> findProjectsAndTasksByUser(@Param("email") String email);

    // Conta a quantidade de tarefas atrasadas de um usuário logado (tarefas não concluídas com data de término anterior à data atual),
    // excluindo as tarefas que foram marcadas como deletadas
    @Query("SELECT COUNT(t) FROM Task t " +
            "JOIN t.integrantes u " +
            "WHERE u.email = :email AND t.endDate < CURRENT_DATE " +
            "AND t.status <> 'DONE' AND t.deleted = false")
    Long countLateTasksByUser(@Param("email") String email);

}
