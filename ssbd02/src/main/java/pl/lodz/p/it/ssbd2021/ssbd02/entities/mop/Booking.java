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
@Table(name = "Booking")
@NamedQueries({
        @NamedQuery(name = "Booking.findAll", query = "SELECT b FROM Booking b"),
        @NamedQuery(name = "Booking.findById", query = "SELECT b FROM Booking b WHERE b.id = :id"),
        @NamedQuery(name = "Booking.findByVersion", query = "SELECT b FROM Booking b WHERE b.version = :version"),
        @NamedQuery(name = "Booking.findByCruise", query = "SELECT b FROM Booking b WHERE b.cruise = :cruise"),
        @NamedQuery(name = "Booking.findByAccount", query = "SELECT b FROM Booking b WHERE b.account = :account"),
        @NamedQuery(name = "Booking.findByNumberOfPeople", query = "SELECT b FROM Booking b WHERE b.numberOfPeople = :numberOfPeople"),
        @NamedQuery(name = "Booking.findByCabin", query = "SELECT b FROM Booking b WHERE b.cabin = :cabin"),
        @NamedQuery(name = "Booking.findByVehicleType", query = "SELECT b FROM Booking b WHERE b.vehicleType = :vehicleType"),
        @NamedQuery(name = "Booking.findByPrice", query = "SELECT b FROM Booking b WHERE b.price = :price"),
        @NamedQuery(name = "Booking.findByNumber", query = "SELECT b FROM Booking b WHERE b.number = :number"),
        @NamedQuery(name = "Booking.findByCreationDate", query = "SELECT b FROM Booking b WHERE b.creationDate = :creationDate")
})
@Data
@NoArgsConstructor
public class Booking extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "cruise", nullable = false, updatable = false, referencedColumnName = "id")
    private Cruise cruise;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "account", nullable = false, updatable = false, referencedColumnName = "id")
    private Account account;

    @NotNull
    @Column(name = "number_of_people", nullable = false, updatable = false)
    private Integer numberOfPeople;

    @NotNull
    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "cabin", nullable = true, updatable = false, referencedColumnName = "id")
    private Cabin cabin;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "vehicle_type", nullable = false, updatable = false, referencedColumnName = "id")
    private VehicleType vehicleType;

    @NotNull
    @Column(name = "price", nullable = false, updatable = false)
    private Double price;

    @NotNull
    @Column(name = "number", nullable = false, unique = true, updatable = false, length = 10)
    private String number;

    @NotNull
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Timestamp creationDate = Timestamp.from(Instant.now());

}
