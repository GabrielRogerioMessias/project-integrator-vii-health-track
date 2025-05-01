package org.projetointegrador.unifio.projectintegratorviibackend.models.patient;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.projetointegrador.unifio.projectintegratorviibackend.models.glucose.BloodGlucose;
import org.projetointegrador.unifio.projectintegratorviibackend.models.pressure.Pressure;
import org.projetointegrador.unifio.projectintegratorviibackend.models.User;

import java.util.Date;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date birth;
    private String CPF;
    private String phone;
    private Double weight;
    private Double height;
    private Date createdAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

    @OneToMany(mappedBy = "patient")
    private List<BloodGlucose> glucoseList;
    @OneToMany(mappedBy = "patient")
    private List<Pressure> pressureList;
}
