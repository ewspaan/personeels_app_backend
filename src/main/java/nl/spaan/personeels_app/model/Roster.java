package nl.spaan.personeels_app.model;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "roster")
public class Roster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    LocalDate startDateWeek;

//    @OneToMany (
//            mappedBy = "roster",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true)
//    private List<Shift> shift;
}
