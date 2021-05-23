package pl.lodz.p.it.ssbd2021.ssbd02.entities.mok;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Table(name = "One_time_url", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"account", "action_type"})
})
@NamedQueries({
        @NamedQuery(name = "OneTimeUrl.findAll", query = "SELECT o FROM OneTimeUrl o"),
        @NamedQuery(name = "OneTimeUrl.findById", query = "SELECT o FROM OneTimeUrl o WHERE o.id = :id"),
        @NamedQuery(name = "OneTimeUrl.findByUrl", query = "SELECT o FROM OneTimeUrl o WHERE o.url = :url"),
        @NamedQuery(name = "OneTimeUrl.findByAccount", query = "SELECT o FROM OneTimeUrl o WHERE o.account = :account"),
        @NamedQuery(name = "OneTimeUrl.findByAccountLogin", query = "SELECT o FROM OneTimeUrl o WHERE o.account.login = :login"),
        @NamedQuery(name = "OneTimeUrl.findByNewEmail", query = "SELECT o FROM OneTimeUrl o WHERE o.newEmail = :newEmail"),
        @NamedQuery(name = "OneTimeUrl.findExpiredUrl", query = "SELECT o FROM OneTimeUrl o WHERE o.expireDate < current_timestamp")
})
@Data
@NoArgsConstructor
public class OneTimeUrl extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @NotBlank
    @Column(name = "url", nullable = false, unique = true, updatable = false, length = 32)
    private String url;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "account", nullable = false, updatable = false, referencedColumnName = "id")
    private Account account;

    @NotBlank
    @Column(name = "action_type", nullable = false, updatable = false, length = 6)
    private String actionType;

    @Size(max = 70, message = "Email address can have a maximum of 70 characters")
    @Email(message = "Provided email address is not valid")
    @Column(name = "new_email", nullable = true, updatable = true, length = 70)
    private String newEmail;

    @NotNull
    @Future
    @Column(name = "expire_date", nullable = false, updatable = true)
    private Timestamp expireDate;

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

