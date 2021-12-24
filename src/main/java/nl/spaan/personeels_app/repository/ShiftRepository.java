package nl.spaan.personeels_app.repository;

import nl.spaan.personeels_app.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface ShiftRepository extends JpaRepository<Shift , Long> {

    List<Shift> findByWeekDayId(Long id);

    List<Shift> findByStartTimeAndEndTimeAndMultiply(LocalTime startTime, LocalTime endTime, int multiply);

    List<Shift> findByStandardRosterTimeId(Long id);
}
