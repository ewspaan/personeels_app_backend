package nl.spaan.personeels_app.repository;

import nl.spaan.personeels_app.model.Company;
import nl.spaan.personeels_app.model.EWeekDay;
import nl.spaan.personeels_app.model.StandardRosterDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandardRosterDayRepository extends JpaRepository<StandardRosterDay, Long> {

    Boolean existsByDay(String day);

    StandardRosterDay findByDay(String day);

    StandardRosterDay findByDayAndCompany(String day, Company company);

    Boolean existsByDayAndCompany(String day, Company company);

    List<StandardRosterDay> findAllByCompany(Company company);
}
