package pl.lodz.p.it.ssbd2021.ssbd02.model.mok;

import lombok.*;
import lombok.AccessLevel;
import pl.lodz.p.it.ssbd2021.ssbd02.model.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "Account")
@SecondaryTable(name = "Personal_data")
@NamedQueries({
        @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
        @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
        @NamedQuery(name = "Account.findByVersion", query = "SELECT a FROM Account a WHERE a.version = :version"),
        @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
        @NamedQuery(name = "Account.findByPassword", query = "SELECT a FROM Account a WHERE a.password = :password"),
        @NamedQuery(name = "Account.findByActive", query = "SELECT a FROM Account a WHERE a.active = :active"),
        @NamedQuery(name = "Account.findByConfirmed", query = "SELECT a FROM Account a WHERE a.confirmed = :confirmed"),

        @NamedQuery(name = "Account.findByFirstName", query = "SELECT a FROM Account a WHERE a.firstName = :firstName"),
        @NamedQuery(name = "Account.findByLastName", query = "SELECT a FROM Account a WHERE a.lastName = :lastName"),
        @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email"),
        @NamedQuery(name = "Account.findByLanguage", query = "SELECT a FROM Account a WHERE a.language = :language"),
        @NamedQuery(name = "Account.findByTimeZone", query = "SELECT a FROM Account a WHERE a.timeZone = :timeZone"),
        @NamedQuery(name = "Account.findByModificationDate", query = "SELECT a FROM Account a WHERE a.modificationDate = :modificationDate"),
        @NamedQuery(name = "Account.findByModifiedBy", query = "SELECT a FROM Account a WHERE a.modifiedBy = :modifiedBy"),
        @NamedQuery(name = "Account.findByCreationDate", query = "SELECT a FROM Account a WHERE a.creationDate = :creationDate"),
        @NamedQuery(name = "Account.findByLastKnownGoodLogin", query = "SELECT a FROM Account a WHERE a.lastKnownGoodLogin = :lastKnownGoodLogin"),
        @NamedQuery(name = "Account.findByLastKnownGoodLoginIp", query = "SELECT a FROM Account a WHERE a.lastKnownGoodLoginIp = :lastKnownGoodLoginIp"),
        @NamedQuery(name = "Account.findByLastKnownBadLogin", query = "SELECT a FROM Account a WHERE a.lastKnownBadLogin = :lastKnownBadLogin"),
        @NamedQuery(name = "Account.findByLastKnownBadLoginIp", query = "SELECT a FROM Account a WHERE a.lastKnownBadLoginIp = :lastKnownBadLoginIp")
})
@Data
@NoArgsConstructor
public class Account extends AbstractEntity implements Serializable {

    //region Account section
    @NotNull
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(name = "login", nullable = false, unique = true, updatable = false, length = 30)
    private String login;

    @NotNull
    @Column(name = "password", nullable = false, updatable = true, length = 128)
    @ToString.Exclude
    private String password;

    @NotNull
    @Column(name = "active", nullable = false, updatable = true)
    private Boolean active;

    @NotNull
    @Column(name = "confirmed", nullable = false, updatable = true)
    private Boolean confirmed;
    //endregion


    //region Personal data section
    @NotNull
    @Column(name = "first_name", nullable = false, updatable = true, length = 30, table = "Personal_data")
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false, updatable = true, length = 50, table = "Personal_data")
    private String lastName;

    @NotNull
    @Column(name = "email", unique = true, nullable = false, updatable = true, length = 70, table = "Personal_data")
    private String email;

    @Column(name = "language", nullable = true, updatable = true, length = 5, table = "Personal_data")
    private String language;

    @Column(name = "time_zone", nullable = true, updatable = true, length = 10, table = "Personal_data")
    private String timeZone;

    @Column(name = "modification_date", nullable = true, updatable = true, table = "Personal_data")
    private Timestamp modificationDate;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "modified_by", nullable = true, updatable = true, referencedColumnName = "id", table = "Personal_data")
    private Account modifiedBy;

    @NotNull
    @Column(name = "creation_date", nullable = false, updatable = false, table = "Personal_data")
    private Timestamp creationDate;

    @Column(name = "last_known_good_login", nullable = true, updatable = true, table = "Personal_data")
    private Timestamp lastKnownGoodLogin;

    @Column(name = "last_known_good_login_ip", nullable = true, updatable = true, length = 15, table = "Personal_data")
    private String lastKnownGoodLoginIp;

    @Column(name = "last_known_bad_login", nullable = true, updatable = true, table = "Personal_data")
    private Timestamp lastKnownBadLogin;

    @Column(name = "last_known_bad_login_ip", nullable = true, updatable = true, length = 15, table = "Personal_data")
    private String lastKnownBadLoginIp;
    //endregion

}
