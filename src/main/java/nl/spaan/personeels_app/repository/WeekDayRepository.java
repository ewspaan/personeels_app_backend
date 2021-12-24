package nl.spaan.personeels_app.repository;

import nl.spaan.personeels_app.model.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeekDayRepository  extends JpaRepository<WeekDay , Long> {
    List<WeekDay> findByWeekRosterId(Long id);
}
