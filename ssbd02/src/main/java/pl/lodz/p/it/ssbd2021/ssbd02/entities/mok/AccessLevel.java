package pl.lodz.p.it.ssbd2021.ssbd02.entities.mok;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "Access_level", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"account", "level"})
})
@NamedQueries({
        @NamedQuery(name = "AccessLevel.findAll", query = "SELECT l FROM AccessLevel l"),
        @NamedQuery(name = "AccessLevel.findById", query = "SELECT l FROM AccessLevel l WHERE l.id = :id"),
        @NamedQuery(name = "AccessLevel.findByLevel", query = "SELECT l FROM AccessLevel l WHERE l.level = :level"),
        @NamedQuery(name = "AccessLevel.findByAccount", query = "SELECT l FROM AccessLevel l WHERE l.account = :account"),
        @NamedQuery(name = "AccessLevel.findByLogin", query = "SELECT l FROM AccessLevel l WHERE l.account.login = :login"),
        @NamedQuery(name = "AccessLevel.findAllActiveByAccount",
                query = "SELECT l FROM AccessLevel l WHERE l.active = true AND l.account = :account")
})
@Data
@NoArgsConstructor
public class AccessLevel extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Size(max = 16, message = "Level can have a maximum of 16 characters")
    @Column(name = "level", nullable = false, updatable = false, length = 16)
    private String level;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "account", nullable = false, updatable = false, referencedColumnName = "id")
    private Account account;

    @Column(name = "active", nullable = false, updatable = true)
    private Boolean active = true;

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
        return super.getSummary() + " account login: " + getAccount().getLogin() + " access level: " + getLevel() + " ";
    }
}
