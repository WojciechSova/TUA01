package pl.lodz.p.it.ssbd2021.ssbd02.entities.auth;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "Auth_view")
@NamedQueries({
        @NamedQuery(name = "AuthView.findAll", query = "SELECT av FROM AuthView av"),
        @NamedQuery(name = "AuthView.findByLogin", query = "SELECT av FROM AuthView av WHERE av.login = :login"),
        @NamedQuery(name = "AuthView.findLevelByCredentials", query = "SELECT av.level FROM AuthView av WHERE av.login = :login AND av.password = :password")
})
@Getter
@NoArgsConstructor
public class AuthView implements Serializable {

    @NotBlank
    @Id
    @Column(name = "login")
    private String login;

    @NotBlank
    @Size(min = 8, message = "Password must have at least 8 characters")
    @Column(name = "password")
    private String password;

    @NotBlank
    @Id
    @Column(name = "level")
    private String level;

}
