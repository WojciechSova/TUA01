package pl.lodz.p.it.ssbd2021.ssbd02.entities.mok;

import lombok.*;
import pl.lodz.p.it.ssbd2021.ssbd02.entities.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.*;
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
        @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email"),
        @NamedQuery(name = "Account.findByConfirmed", query = "SELECT a FROM Account a WHERE a.confirmed = :confirmed")
})
@Data
@NoArgsConstructor
public class Account extends AbstractEntity implements Serializable {

    //region Account section
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 30, message = "Login can have a maximum of 30 characters")
    @Column(name = "login", nullable = false, unique = true, updatable = false, length = 30)
    private String login;

    @NotBlank
    @Size(min = 128, max = 128, message = "Password must have 128 characters")
    @Column(name = "password", nullable = false, updatable = true, length = 128)
    @ToString.Exclude
    private String password;

    @Column(name = "active", nullable = false, updatable = true)
    private Boolean active = true;

    @Column(name = "confirmed", nullable = false, updatable = true)
    private Boolean confirmed = false;
    //endregion


    //region Personal data section
    @NotBlank
    @Size(max = 30, message = "First name can have a maximum of 30 characters")
    @Column(name = "first_name", nullable = false, updatable = true, length = 30, table = "Personal_data")
    private String firstName;

    @NotBlank
    @Size(max = 50, message = "Last name can have a maximum of 50 characters")
    @Column(name = "last_name", nullable = false, updatable = true, length = 50, table = "Personal_data")
    private String lastName;

    @NotBlank
    @Size(max = 70, message = "Email address can have a maximum of 70 characters")
    @Email(message = "Provided email address is not valid")
    @Column(name = "email", nullable = false, unique = true, updatable = true, length = 70, table = "Personal_data")
    private String email;

    @Size(min = 3, max = 15, message = "Phone number must have between 3 and 15 characters")
    @Column(name = "phone_number", nullable = true, unique = true, updatable = true, length = 15, table = "Personal_data")
    private String phoneNumber;

    @Pattern(regexp = "[a-z]{2}-[A-Z]{2}|[a-z]{2}", message = "Language must be 2 lower case letters or 2 lower case letters, a \"-\" and 2 capital letters")
    @Column(name = "language", nullable = false, updatable = true, length = 5, table = "Personal_data")
    private String language = "en";

    @Pattern(regexp = "[+-]0[0-9]:00|-1[0-2]:00|[+]1[0-4]:00", message = "Invalid time zone format, should be like \"+\\-00:00\"")
    @Column(name = "time_zone", nullable = false, updatable = true, length = 6, table = "Personal_data")
    private String timeZone = "+00:00";

    @PastOrPresent
    @Column(name = "modification_date", nullable = true, updatable = true, table = "Personal_data")
    private Timestamp modificationDate;

    @ManyToOne(optional = true, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "modified_by", nullable = true, updatable = true, referencedColumnName = "id", table = "Personal_data")
    private Account modifiedBy;

    @PastOrPresent
    @Column(name = "creation_date", nullable = false, updatable = false, table = "Personal_data")
    private Timestamp creationDate = Timestamp.from(Instant.now());

    @PastOrPresent
    @Column(name = "last_known_good_login", nullable = true, updatable = true, table = "Personal_data")
    private Timestamp lastKnownGoodLogin;

    @Size(max = 39, message = "Last known good login IP can have a maximum of 39 characters")
    @Column(name = "last_known_good_login_ip", nullable = true, updatable = true, length = 15, table = "Personal_data")
    private String lastKnownGoodLoginIp;

    @PastOrPresent
    @Column(name = "last_known_bad_login", nullable = true, updatable = true, table = "Personal_data")
    private Timestamp lastKnownBadLogin;

    @Size(max = 39, message = "Last known bad login IP can have a maximum of 39 characters")
    @Column(name = "last_known_bad_login_ip", nullable = true, updatable = true, length = 15, table = "Personal_data")
    private String lastKnownBadLoginIp;

    @PositiveOrZero
    @Column(name = "number_of_bad_logins", nullable = false, updatable = true, table = "Personal_data")
    private Integer numberOfBadLogins = 0;
    //endregion

}
