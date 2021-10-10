package nl.spaan.personeels_app.repository;

import nl.spaan.personeels_app.model.StandardRosterDay;
import nl.spaan.personeels_app.model.StandardRosterTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface StandardRosterTimeRepository extends JpaRepository<StandardRosterTime, Long> {
    

    StandardRosterTime findByStartTimeHourAndStartTimeMinuteAndEndTimeHourAndEndTimeMinute(String startTimeHour, String startTimeMinute, String endTimeHour, String endTimeMinute);

    List<StandardRosterTime> findByStandardRosterDayId(Long id);

}
