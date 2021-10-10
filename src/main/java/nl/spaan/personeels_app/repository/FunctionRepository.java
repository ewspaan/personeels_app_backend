package nl.spaan.personeels_app.repository;


import nl.spaan.personeels_app.model.EFunction;
import nl.spaan.personeels_app.model.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long> {

    Optional<Function> findByName(EFunction function);
}
