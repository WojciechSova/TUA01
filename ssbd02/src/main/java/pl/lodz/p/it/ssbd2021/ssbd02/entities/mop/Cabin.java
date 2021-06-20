package pl.lodz.p.it.ssbd2021.ssbd02.entities.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "Cabin", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"ferry", "number"})
})
@NamedQueries({
        @NamedQuery(name = "Cabin.findAll", query = "SELECT c FROM Cabin c"),
        @NamedQuery(name = "Cabin.findById", query = "SELECT c FROM Cabin c WHERE c.id = :id"),
        @NamedQuery(name = "Cabin.findByFerry", query = "SELECT c FROM Cabin c WHERE c.ferry = :ferry"),
        @NamedQuery(name = "Cabin.findByNumber", query = "SELECT c FROM Cabin c WHERE c.number = :number"),
        @NamedQuery(name = "Cabin.findByFerryAndNumber", query = "SELECT c FROM Cabin c WHERE c.ferry = :ferry AND c.number = :number"),
        @NamedQuery(name = "Cabin.findOccupiedCabinsOnCruise", query = "SELECT c FROM Cabin c, Booking b WHERE b.cabin = c AND b.cruise = :cruise"),
        @NamedQuery(name = "Cabin.findCabinsOnCruise", query = "SELECT c FROM Cabin c, Cruise cr WHERE cr = :cruise AND c.ferry = cr.ferry")
})
@Data
@NoArgsConstructor
public class Cabin extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ferry", nullable = false, updatable = false, referencedColumnName = "id")
    private Ferry ferry;

    @NotNull
    @Positive(message = "Cabin capacity must be positive")
    @Column(name = "capacity", nullable = false, updatable = true)
    private Integer capacity;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "cabin_type", nullable = false, updatable = true, referencedColumnName = "id")
    private CabinType cabinType;

    @Pattern(regexp = "[A-Z][0-9]{3}", message = "Cabin number must be a capital letter and 3 digits")
    @Column(name = "number", nullable = false, updatable = false, length = 4)
    private String number;

    @PastOrPresent
    @Column(name = "modification_date", nullable = true, updatable = true)
    private Timestamp modificationDate;

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "modified_by", nullable = true, updatable = true, referencedColumnName = "id")
    private Account modifiedBy;

    @PastOrPresent
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Timestamp creationDate = Timestamp.from(Instant.now());

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "created_by", nullable = true, updatable = false, referencedColumnName = "id")
    private Account createdBy;

    @Override
    public String getSummary() {
        return super.getSummary() + " number: " + getNumber() + " ";
    }
}
