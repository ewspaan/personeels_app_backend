package nl.spaan.personeels_app.repository;


import nl.spaan.personeels_app.model.ERole;
import nl.spaan.personeels_app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

        Optional<Role> findByName(ERole name);

}

