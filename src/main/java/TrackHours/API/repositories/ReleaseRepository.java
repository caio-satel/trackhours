package TrackHours.API.repositories;

import TrackHours.API.entities.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long> {

    @Query("SELECT r FROM Release r JOIN r.user u WHERE u.email = :email")
    List<Release> findReleasesByUserLogged(@Param("email") String email);
}
