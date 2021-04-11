package pl.lodz.p.it.ssbd2021.ssbd02.entities.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "Cruise")
@NamedQueries({
        @NamedQuery(name = "Cruise.findAll", query = "SELECT cr FROM Cruise cr"),
        @NamedQuery(name = "Cruise.findById", query = "SELECT cr FROM Cruise cr WHERE cr.id = :id"),
        @NamedQuery(name = "Cruise.findByVersion", query = "SELECT cr FROM Cruise cr WHERE cr.version = :version"),
        @NamedQuery(name = "Cruise.findByStartDate", query = "SELECT cr FROM Cruise cr WHERE cr.startDate = :startDate"),
        @NamedQuery(name = "Cruise.findByEndDate", query = "SELECT cr FROM Cruise cr WHERE cr.endDate = :endDate"),
        @NamedQuery(name = "Cruise.findByRoute", query = "SELECT cr FROM Cruise cr WHERE cr.route = :route"),
        @NamedQuery(name = "Cruise.findByFerry", query = "SELECT cr FROM Cruise cr WHERE cr.ferry = :ferry"),
        @NamedQuery(name = "Cruise.findByNumber", query = "SELECT cr FROM Cruise cr WHERE cr.number = :number"),
        @NamedQuery(name = "Cruise.findByModificationDate", query = "SELECT cr FROM Cruise cr WHERE cr.modificationDate = :modificationDate"),
        @NamedQuery(name = "Cruise.findByModifiedBy", query = "SELECT cr FROM Cruise cr WHERE cr.modifiedBy = :modifiedBy"),
        @NamedQuery(name = "Cruise.findByCreationDate", query = "SELECT cr FROM Cruise cr WHERE cr.creationDate = :creationDate"),
        @NamedQuery(name = "Cruise.findByCreatedBy", query = "SELECT cr FROM Cruise cr WHERE cr.createdBy = :createdBy"),

})
@Data
@NoArgsConstructor
public class Cruise extends AbstractEntity implements Serializable {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(name = "start_date", nullable = false, updatable = true)
    private Timestamp startDate;

    @NotNull
    @Column(name = "end_date", nullable = false, updatable = true)
    private Timestamp endDate;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "route", nullable = false, updatable = true, referencedColumnName = "id")
    private Route route;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ferry", nullable = false, updatable = true, referencedColumnName = "id")
    private Ferry ferry;

    @NotNull
    @Column(name = "number", nullable = false, updatable = true, length = 12)
    private String number;

    @Column(name = "modification_date", nullable = true, updatable = true)
    private Timestamp modificationDate;

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "modified_by", nullable = true, updatable = true, referencedColumnName = "id")
    private Account modifiedBy;

    @NotNull
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Timestamp creationDate = Timestamp.from(Instant.now());

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "created_by", nullable = true, updatable = false, referencedColumnName = "id")
    private Account createdBy;


}
