package pl.lodz.p.it.ssbd2021.ssbd02.entities.mop;

import lombok.*;
import lombok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.AbstractEntity;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.mok.Account;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "Seaport")
@NamedQueries({
        @NamedQuery(name = "Seaport.findAll", query = "SELECT s FROM Seaport s"),
        @NamedQuery(name = "Seaport.findById", query = "SELECT s FROM Seaport s WHERE s.id = :id"),
        @NamedQuery(name = "Seaport.findByCity", query = "SELECT s FROM Seaport s WHERE s.city = :city"),
        @NamedQuery(name = "Seaport.findByCode", query = "SELECT s FROM Seaport s WHERE s.code = :code")
})
@Data
@NoArgsConstructor
public class Seaport extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Size(max = 30, message = "City can have a maximum of 30 characters")
    @Column(name = "city", nullable = false, unique = true, updatable = true, length = 30)
    private String city;

    @NotBlank
    @Pattern(regexp = "[A-Z]{3}", message = "Seaport code must be 3 capital letters")
    @Column(name = "code", nullable = false, unique = true, updatable = false, length = 3)
    private String code;

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
