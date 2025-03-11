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

    @Query("SELECT r FROM Release r JOIN r.user u WHERE u.email = :email AND r.deleted = false")
    List<Release> findReleasesByUserLogged(@Param("email") String email);

    // Filtra todas as releases não deletadas
    @Query("SELECT r FROM Release r WHERE r.deleted = false")
    List<Release> findAllNotDeleted();

    // Filtra uma release não deletada pelo ID
    @Query("SELECT r FROM Release r WHERE r.id = :id AND r.deleted = false")
    Optional<Release> findByIdAndNotDeleted(@Param("id") Long id);
}
