package pl.lodz.p.it.ssbd2021.ssbd02.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Entity
@Table(name = "Client_data")
@DiscriminatorValue("CLIENT")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "ClientData.findAll", query = "SELECT c FROM ClientData c"),
        @NamedQuery(name = "ClientData.findById", query = "SELECT c FROM ClientData c WHERE c.id =  :id"),
        @NamedQuery(name = "ClientData.findByPhoneNumber", query = "SELECT c FROM ClientData c WHERE c.phoneNumber = :phoneNumber")
})
public class ClientData extends AccessLevel implements Serializable {

    @NotNull
    @Column(name = "phone_number", nullable = false, updatable = true, length = 11)
    private String phoneNumber;



}
