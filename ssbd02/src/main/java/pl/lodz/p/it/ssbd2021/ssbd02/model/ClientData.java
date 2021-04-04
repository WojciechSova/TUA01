package pl.lodz.p.it.ssbd2021.ssbd02.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "Client_data")
@DiscriminatorValue("CLIENT")
@ToString(callSuper = true)
@NamedQueries({
        @NamedQuery(name = "ClientData.findAll", query = "SELECT c FROM ClientData c"),
        @NamedQuery(name = "ClientData.findById", query = "SELECT c FROM ClientData c WHERE c.id =  :id"),
        @NamedQuery(name = "ClientData.findByPhoneNumber", query = "SELECT c FROM ClientData c WHERE c.phoneNumber = :phoneNumber")
})
public class ClientData extends AccessLevel implements Serializable {

    @NotNull
    @Getter @Setter
    @Column(name = "phone_number", nullable = false, updatable = true, length = 11)
    private String phoneNumber;

    public ClientData() {

    }

}
