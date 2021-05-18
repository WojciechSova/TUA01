package pl.lodz.p.it.ssbd2021.ssbd02.entities.mop;

import lombok.*;
import lombok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "Ferry")
@NamedQueries({
        @NamedQuery(name = "Ferry.findAll", query = "SELECT f FROM Ferry f"),
        @NamedQuery(name = "Ferry.findById", query = "SELECT f FROM Ferry f WHERE f.id = :id"),
        @NamedQuery(name = "Ferry.findByName", query = "SELECT f FROM Ferry f WHERE f.name = :name")
})
@Data
@NoArgsConstructor
public class Ferry extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Size(max = 30, message = "Ferry name can have a maximum of 30 characters")
    @Column(name = "name", nullable = false, unique = true, updatable = false, length = 30)
    private String name;

    @NotNull
    @PositiveOrZero(message = "Ferry vehicle capacity must be positive or zero")
    @Column(name = "vehicle_capacity", nullable = false, updatable = true)
    private Integer vehicleCapacity;

    @NotNull
    @Positive(message = "Ferry on deck capacity must be positive")
    @Column(name = "on_deck_capacity", nullable = false, updatable = true)
    private Integer onDeckCapacity;

    @Column(name = "modification_date", nullable = true, updatable = true)
    private Timestamp modificationDate;

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "modified_by", nullable = true, updatable = true, referencedColumnName = "id")
    private Account modifiedBy;

    @Column(name = "creation_date", nullable = false, updatable = false)
    private Timestamp creationDate = Timestamp.from(Instant.now());

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "created_by", nullable = true, updatable = false, referencedColumnName = "id")
    private Account createdBy;

}
