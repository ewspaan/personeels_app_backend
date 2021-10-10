package nl.spaan.personeels_app.model;


import javax.persistence.*;

@Entity
public class Function {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EFunction name;

    @OneToOne(mappedBy = "function")
    private StandardRosterDay standardRosterDay;

    public Function() {
    }

    public Function(EFunction name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EFunction getName() {
        return name;
    }

    public void setName(EFunction name) {
        this.name = name;
    }

    public StandardRosterDay getStandardRosterDay() {
        return standardRosterDay;
    }

    public void setStandardRosterDay(StandardRosterDay standardRosterDay) {
        this.standardRosterDay = standardRosterDay;
    }
}
