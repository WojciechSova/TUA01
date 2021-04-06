package pl.lodz.p.it.ssbd2021.ssbd02.model.mop;

import lombok.AccessLevel;
import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mok.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "Route")
@NamedQueries({
        @NamedQuery(name = "Route.findAll", query = "SELECT r FROM Route r"),
        @NamedQuery(name = "Route.findById", query = "SELECT r FROM Route r WHERE r.id = :id"),
        @NamedQuery(name = "Route.findByVersion", query = "SELECT r FROM Route r WHERE r.version = :version"),
        @NamedQuery(name = "Route.findByStart", query = "SELECT r FROM Route r WHERE r.start = :start"),
        @NamedQuery(name = "Route.findByDestination", query = "SELECT r FROM Route r WHERE r.destination = :destination"),
        @NamedQuery(name = "Route.findByCreationDate", query = "SELECT r FROM Route r WHERE r.creationDate = :creationDate"),
        @NamedQuery(name = "Route.findByCreatedBy", query = "SELECT r FROM Route r WHERE r.createdBy = :createdBy"),
})
@Data
@NoArgsConstructor
public class Route implements Serializable {

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
    @Column(name = "version", nullable = false, updatable = true)
    private long version;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "start", nullable = false, updatable = true, referencedColumnName = "id")
    private Seaport start;

    @NotNull
    @ManyToOne(optional = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "destination", nullable = false, updatable = true, referencedColumnName = "id")
    private Seaport destination;

    @NotNull
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Timestamp creationDate;

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "created_by", updatable = false, referencedColumnName = "id")
    private Account createdBy;

}
