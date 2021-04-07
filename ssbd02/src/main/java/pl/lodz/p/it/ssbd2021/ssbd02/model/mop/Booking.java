package pl.lodz.p.it.ssbd2021.ssbd02.model.mop;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mok.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

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
        @NamedQuery(name = "Booking.findByCreationDate", query = "SELECT b FROM Booking b WHERE b.creationDate = :creationDate")
})
@Data
@NoArgsConstructor
public class Booking implements Serializable {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private long id;

    @NotNull
    @Version
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "version", nullable = false, updatable = false)
    private long version;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cruise", nullable = false, updatable = false, referencedColumnName = "id")
    private Cruise cruise;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "account", nullable = false, updatable = false, referencedColumnName = "id")
    private Account account;

    @NotNull
    @Column(name = "number_of_people", nullable = false, updatable = false)
    private int numberOfPeople;

    @NotNull
    @ManyToOne(optional = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cabin", nullable = true, updatable = false, referencedColumnName = "id")
    private Cabin cabin;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "vehicle_type", nullable = false, updatable = false, referencedColumnName = "id")
    private VehicleType vehicleType;

    @NotNull
    @Column(name = "price", nullable = false, updatable = false)
    private double price;

    @NotNull
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Timestamp creationDate;

}
