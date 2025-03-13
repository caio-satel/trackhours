package TrackHours.API.repositories;

import TrackHours.API.entities.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {

    // Consulta os lançamentos de horas de um usuário logado pelo e-mail, excluindo os registros marcados como deletados
    @Query("SELECT r FROM Release r JOIN r.user u WHERE u.email = :email AND r.deleted = false")
    List<Release> findReleasesByUserLogged(@Param("email") String email);

    // Consulta todos os lançamentos de horas que não foram marcados como deletados
    @Query("SELECT r FROM Release r WHERE r.deleted = false")
    List<Release> findAllNotDeleted();

    // Consulta um lançamento de horas pelo ID, garantindo que não esteja marcado como deletado
    @Query("SELECT r FROM Release r WHERE r.id = :id AND r.deleted = false")
    Optional<Release> findByIdAndNotDeleted(@Param("id") Long id);

    // Consulta o nome do usuário, a quantidade de lançamentos realizados e o total de horas trabalhadas,
    // agrupando por nome e filtrando lançamentos e usuários que não estejam marcados como deletados
    @Query("SELECT u.name AS user_name, " +
            "COUNT(r.id) AS launch_count, " +
            "SUM(TIMESTAMPDIFF(HOUR, r.startTime, r.endTime)) AS total_hours " +
            "FROM Release r " +
            "JOIN User u ON r.user.id = u.id " +
            "WHERE r.deleted = false " +
            "AND u.deleted = false " +
            "GROUP BY u.name")
    List<Object[]> findLaunchesAndTotalHoursByUser();
}