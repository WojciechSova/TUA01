package pl.lodz.p.it.ssbd2021.ssbd02.model.mop;

import lombok.*;
import lombok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.model.AbstractEntity;
import pl.lodz.p.it.ssbd2021.ssbd02.model.mok.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "Seaport")
@NamedQueries({
        @NamedQuery(name = "Seaport.findAll", query = "SELECT s FROM Seaport s"),
        @NamedQuery(name = "Seaport.findById", query = "SELECT s FROM Seaport s WHERE s.id = :id"),
        @NamedQuery(name = "Seaport.findByVersion", query = "SELECT s FROM Seaport s WHERE s.version = :version"),
        @NamedQuery(name = "Seaport.findByCity", query = "SELECT s FROM Seaport s WHERE s.city = :city"),
        @NamedQuery(name = "Seaport.findByModificationDate", query = "SELECT s FROM Seaport s WHERE s.modificationDate = :modificationDate"),
        @NamedQuery(name = "Seaport.findByModifiedBy", query = "SELECT s FROM Seaport s WHERE s.modifiedBy = :modifiedBy"),
        @NamedQuery(name = "Seaport.findByCreationDate", query = "SELECT s FROM Seaport s WHERE s.creationDate = :creationDate"),
        @NamedQuery(name = "Seaport.findByCreatedBy", query = "SELECT s FROM Seaport s WHERE s.createdBy = :createdBy")
})
@Data
@NoArgsConstructor
public class Seaport extends AbstractEntity implements Serializable {

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(name = "city", nullable = false, updatable = true, length = 30)
    private String city;

    @NotNull
    @Column(name = "code", nullable = false, updatable = true, length = 3)
    private String code;

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
