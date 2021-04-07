package pl.lodz.p.it.ssbd2021.ssbd02.model.mop;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mok.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@NamedQueries({
        @NamedQuery(name = "Cabin.findAll", query = "SELECT c FROM Cabin c"),
        @NamedQuery(name = "Cabin.findById", query = "SELECT c FROM Cabin c WHERE c.id = :id"),
        @NamedQuery(name = "Cabin.findByVersion", query = "SELECT c FROM Cabin c WHERE c.version = :version"),
        @NamedQuery(name = "Cabin.findByFerry", query = "SELECT c FROM Cabin c WHERE c.ferry = :ferry"),
        @NamedQuery(name = "Cabin.findByCapacity", query = "SELECT c FROM Cabin c WHERE c.capacity = :capacity"),
        @NamedQuery(name = "Cabin.findByCabinType", query = "SELECT c FROM Cabin c WHERE c.cabinType = :cabinType"),
        @NamedQuery(name = "Cabin.findByModificationDate", query = "SELECT c FROM Cabin c WHERE c.modificationDate = :modificationDate"),
        @NamedQuery(name = "Cabin.findBModifiedBy", query = "SELECT c FROM Cabin c WHERE c.modifiedBy = :modifiedBy"),
        @NamedQuery(name = "Cabin.findByCreationDate", query = "SELECT c FROM Cabin c WHERE c.creationDate = :creationDate"),
        @NamedQuery(name = "Cabin.findByCreatedBy", query = "SELECT c FROM Cabin c WHERE c.createdBy = :createdBy")

})
@Data
@NoArgsConstructor
public class Cabin implements Serializable {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Version
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "version", nullable = false, updatable = true)
    private Long version;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ferry", nullable = false, updatable = true, referencedColumnName = "id")
    private Ferry ferry;

    @NotNull
    @Column(name = "capacity", nullable = false, updatable = true)
    private Integer capacity;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cabin_type", nullable = false, updatable = true, referencedColumnName = "id")
    private CabinType cabinType;

    @NotNull
    @Column(name = "number", nullable = false, updatable = true, length = 4)
    private String number;

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
