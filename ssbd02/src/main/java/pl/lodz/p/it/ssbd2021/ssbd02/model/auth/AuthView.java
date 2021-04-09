package pl.lodz.p.it.ssbd2021.ssbd02.model.auth;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "Auth_view")
@NamedQueries({
        @NamedQuery(name = "AuthView.findAll", query = "SELECT av FROM AuthView av"),
        @NamedQuery(name = "AuthView.findByLogin", query = "SELECT av FROM AuthView av WHERE av.login = :login"),
        @NamedQuery(name = "AuthView.findByLevel", query = "SELECT av FROM AuthView av WHERE av.level = :level"),
        @NamedQuery(name = "AuthView.findByCredentials", query = "SELECT av FROM AuthView av WHERE av.login = :login AND av.password = :password")
})
@Getter
@NoArgsConstructor
public class AuthView implements Serializable {

    @NotNull
    @Id
    @Column(name = "login")
    private String login;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Id
    @Column(name = "level")
    private String level;

}
