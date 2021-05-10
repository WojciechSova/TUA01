package pl.lodz.p.it.ssbd2021.ssbd02.entities.mok;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
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
public class OneTimeUrl implements Serializable {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private long id;

    @NotNull
    @Column(name = "url", nullable = false, unique = true, updatable = false, length = 32)
    private String url;

    @NotNull
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "account", nullable = false, updatable = false, referencedColumnName = "id")
    private Account account;

    @NotNull
    @Column(name = "action_type", nullable = false, updatable = false, length = 6)
    private String actionType;

    @Column(name = "new_email", nullable = true, updatable = false, length = 70)
    private String newEmail;

    @NotNull
    @Column(name = "expire_date", nullable = false, updatable = false)
    private Timestamp expireDate;
}

