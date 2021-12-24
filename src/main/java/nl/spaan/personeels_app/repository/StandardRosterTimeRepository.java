package nl.spaan.personeels_app.repository;

import nl.spaan.personeels_app.model.Company;
import nl.spaan.personeels_app.model.StandardRosterTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandardRosterTimeRepository extends JpaRepository<StandardRosterTime, Long> {


    List<StandardRosterTime> findByStandardRosterDayId(Long id);

}
