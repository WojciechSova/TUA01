package pl.lodz.p.it.ssbd2021.ssbd02.model.mop;

import lombok.*;
import lombok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mok.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "Ferry")
@NamedQueries({
        @NamedQuery(name = "Ferry.findAll", query = "SELECT f FROM Ferry f"),
        @NamedQuery(name = "Seaport.findById", query = "SELECT f FROM Ferry f WHERE f.id = :id"),
        @NamedQuery(name = "Seaport.findByVersion", query = "SELECT f FROM Ferry f WHERE f.version = :version"),
        @NamedQuery(name = "Seaport.findByName", query = "SELECT f FROM Ferry f WHERE f.name = :name"),
        @NamedQuery(name = "Seaport.findByVehicleCapacity", query = "SELECT f FROM Ferry f WHERE f.vehicleCapacity = :vehicleCapacity"),
        @NamedQuery(name = "Seaport.findByOnDeckCapacity", query = "SELECT f FROM Ferry f WHERE f.onDeckCapacity = :onDeckCapacity"),
        @NamedQuery(name = "Seaport.findByModificationDate", query = "SELECT f FROM Ferry f WHERE f.modificationDate = :modificationDate"),
        @NamedQuery(name = "Seaport.findByModifiedBy", query = "SELECT f FROM Ferry f WHERE f.modifiedBy = :modifiedBy"),
        @NamedQuery(name = "Seaport.findByCreationDate", query = "SELECT f FROM Ferry f WHERE f.creationDate = :creationDate"),
        @NamedQuery(name = "Seaport.findByCreatedBy", query = "SELECT f FROM Ferry f WHERE f.createdBy = :createdBy")
})
@Data
@NoArgsConstructor
public class Ferry implements Serializable {

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
    @Column(name = "name", nullable = false, unique = true, length = 30)
    private String name;

    @NotNull
    @Column(name = "vehicle_capacity", nullable = false, updatable = true)
    private int vehicleCapacity;

    @NotNull
    @Column(name = "on_deck_capacity", nullable = false, updatable = true)
    private int onDeckCapacity;

    @Column(name = "modification_date", nullable = true, updatable = true)
    private Timestamp modificationDate;

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "modified_by", nullable = true, updatable = true, referencedColumnName = "id")
    private Account modifiedBy;

    @NotNull
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Timestamp creationDate;

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "created_by", nullable = true, updatable = false, referencedColumnName = "id")
    private Account createdBy;

}
