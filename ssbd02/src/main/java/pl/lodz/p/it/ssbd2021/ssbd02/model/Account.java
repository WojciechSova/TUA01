package pl.lodz.p.it.ssbd2021.ssbd02.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "account")
@NamedQueries({
        @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
        @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
        @NamedQuery(name = "Account.findByVersion", query = "SELECT a FROM Account a WHERE a.version = :version"),
        @NamedQuery(name = "Account.findByLogin", query = "SELECT a FROM Account a WHERE a.login = :login"),
        @NamedQuery(name = "Account.findByFirstName", query = "SELECT a FROM Account a WHERE a.firstName = :firstName"),
        @NamedQuery(name = "Account.findByLastName", query = "SELECT a FROM Account a WHERE a.lastName = :lastName"),
        @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email"),
        @NamedQuery(name = "Account.findByPassword", query = "SELECT a FROM Account a WHERE a.password = :password"),
        @NamedQuery(name = "Account.findByActive", query = "SELECT a FROM Account a WHERE a.active = :active"),
        @NamedQuery(name = "Account.findByConfirmed", query = "SELECT a FROM Account a WHERE a.confirmed = :confirmed"),
        @NamedQuery(name = "Account.findByModificationDate", query = "SELECT a FROM Account a WHERE a.modificationDate = :modificationDate"),
        @NamedQuery(name = "Account.findByCreationDate", query = "SELECT a FROM Account a WHERE a.creationDate = :creationDate"),
        @NamedQuery(name = "Account.findByLanguage", query = "SELECT a FROM Account a WHERE a.language = :language"),
        @NamedQuery(name = "Account.findByLastKnownGoodLogin", query = "SELECT a FROM Account a WHERE a.lastKnownGoodLogin = :lastKnownGoodLogin"),
        @NamedQuery(name = "Account.findByLastKnownGoodLoginIp", query = "SELECT a FROM Account a WHERE a.lastKnownGoodLoginIp = :lastKnownGoodLoginIp"),
        @NamedQuery(name = "Account.findByLastKnownBadLogin", query = "SELECT a FROM Account a WHERE a.lastKnownBadLogin = :lastKnownBadLogin"),
        @NamedQuery(name = "Account.findByLastKnownBadLoginIp", query = "SELECT a FROM Account a WHERE a.lastKnownBadLoginIp = :lastKnownBadLoginIp"),
        @NamedQuery(name = "Account.findByTimeZone", query = "SELECT a FROM Account a WHERE a.timeZone = :timeZone"),
        @NamedQuery(name = "Account.findByAccountByModifiedBy", query = "SELECT a FROM Account a WHERE a.accountByModifiedBy = :accountByModifiedBy"),
        @NamedQuery(name = "Account.findByAccountByCreatedBy", query = "SELECT a FROM Account a WHERE a.accountByCreatedBy = :accountByCreatedBy")
})
@Data
public class Account implements Serializable {

    @NotNull
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(name = "version", nullable = false, updatable = true)
    private Long version;

    @NotNull
    @Column(name = "login", nullable = false, updatable = false, length = 30)
    private String login;

    @NotNull
    @Column(name = "first_name", nullable = false, updatable = true, length = 30)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false, updatable = true, length = 50)
    private String lastName;

    @NotNull
    @Column(name = "email", nullable = false, updatable = true, length = 70)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false, updatable = true, length = 128)
    @ToString.Exclude
    private String password;

    @NotNull
    @Column(name = "active", nullable = false, updatable = true)
    private boolean active;

    @NotNull
    @Column(name = "confirmed", nullable = false, updatable = true)
    private boolean confirmed;

    @Column(name = "modification_date", nullable = true, updatable = true)
    private Timestamp modificationDate;

    @NotNull
    @Column(name = "creation_date", nullable = false, updatable = false)
    private Timestamp creationDate;

    @Column(name = "language", nullable = true, updatable = true, length = 5)
    private String language;

    @Column(name = "last_known_good_login", nullable = true, updatable = true)
    private Timestamp lastKnownGoodLogin;

    @Column(name = "last_known_good_login_ip", nullable = true, updatable = true, length = 15)
    private String lastKnownGoodLoginIp;

    @Column(name = "last_known_bad_login", nullable = true, updatable = true)
    private Timestamp lastKnownBadLogin;

    @Column(name = "last_known_bad_login_ip", nullable = true, updatable = true, length = 15)
    private String lastKnownBadLoginIp;

    @Column(name = "time_zone", nullable = true, updatable = true, length = 10)
    private String timeZone;

    @ManyToOne
    @JoinColumn(name = "modified_by", nullable = true, updatable = true, referencedColumnName = "id")
    private Account accountByModifiedBy;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = true, updatable = false, referencedColumnName = "id")
    private Account accountByCreatedBy;

}
