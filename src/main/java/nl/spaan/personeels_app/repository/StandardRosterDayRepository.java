package nl.spaan.personeels_app.repository;

import nl.spaan.personeels_app.model.Company;
import nl.spaan.personeels_app.model.StandardRosterDay;
import nl.spaan.personeels_app.model.StandardRosterTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StandardRosterDayRepository extends JpaRepository<StandardRosterDay, Long> {


    List<StandardRosterDay> findAllByCompany(Company company);

    StandardRosterDay findByCompanyAndDay(Company company, String day);

    List<StandardRosterTime> findByDay(String day);
}
