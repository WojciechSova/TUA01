package pl.lodz.p.it.ssbd2021.ssbd02.entities.mok;

import lombok.*;
import lombok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "Account")
@SecondaryTable(name = "Personal_data")
@NamedQueries({
        @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
        @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
        @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
        @NamedQuery(name = "Account.findAllActiveAccounts", query = "SELECT a FROM Account a WHERE a.active = :active"),

        @NamedQuery(name = "Account.findByFirstName", query = "SELECT a FROM Account a WHERE a.firstName = :firstName"),
        @NamedQuery(name = "Account.findByLastName", query = "SELECT a FROM Account a WHERE a.lastName = :lastName"),
        @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email")
})
@Data
@NoArgsConstructor
public class Account extends AbstractEntity implements Serializable {

    //region Account section
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(name = "login", nullable = false, unique = true, updatable = false, length = 30)
    private String login;

    @NotNull
    @Column(name = "password", nullable = false, updatable = true, length = 128)
    @ToString.Exclude
    private String password;

    @Column(name = "active", nullable = false, updatable = true)
    private Boolean active = true;

    @Column(name = "confirmed", nullable = false, updatable = true)
    private Boolean confirmed = false;
    //endregion


    //region Personal data section
    @NotNull
    @Column(name = "first_name", nullable = false, updatable = true, length = 30, table = "Personal_data")
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false, updatable = true, length = 50, table = "Personal_data")
    private String lastName;

    @NotNull
    @Column(name = "email", nullable = false, unique = true, updatable = true, length = 70, table = "Personal_data")
    private String email;

    @Column(name = "phone_number", nullable = true, unique = true, updatable = true, length = 11, table = "Personal_data")
    private String phoneNumber;

    @Column(name = "language", nullable = true, updatable = true, length = 5, table = "Personal_data")
    private String language;

    @Column(name = "time_zone", nullable = true, updatable = true, length = 50, table = "Personal_data")
    private String timeZone;

    @Column(name = "modification_date", nullable = true, updatable = true, table = "Personal_data")
    private Timestamp modificationDate;

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "modified_by", nullable = true, updatable = true, referencedColumnName = "id", table = "Personal_data")
    private Account modifiedBy;

    @Column(name = "creation_date", nullable = false, updatable = false, table = "Personal_data")
    private Timestamp creationDate = Timestamp.from(Instant.now());

    @Column(name = "last_known_good_login", nullable = true, updatable = true, table = "Personal_data")
    private Timestamp lastKnownGoodLogin;

    @Column(name = "last_known_good_login_ip", nullable = true, updatable = true, length = 15, table = "Personal_data")
    private String lastKnownGoodLoginIp;

    @Column(name = "last_known_bad_login", nullable = true, updatable = true, table = "Personal_data")
    private Timestamp lastKnownBadLogin;

    @Column(name = "last_known_bad_login_ip", nullable = true, updatable = true, length = 15, table = "Personal_data")
    private String lastKnownBadLoginIp;

    @Column(name = "number_of_bad_logins", nullable = false, updatable = true, table = "Personal_data")
    private Integer numberOfBadLogins = 0;
    //endregion

}
