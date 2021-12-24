package nl.spaan.personeels_app.repository;

import nl.spaan.personeels_app.model.Company;
import nl.spaan.personeels_app.model.WeekRoster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeekRosterRepository extends JpaRepository<WeekRoster, Long> {

    WeekRoster findByCompanyAndWeekAndYear(Company company, int weekNumber, int year);

    boolean existsByCompanyAndWeekAndYear(Company company, int week, int year);
}

