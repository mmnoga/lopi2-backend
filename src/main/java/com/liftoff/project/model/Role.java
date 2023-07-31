package com.liftoff.project.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "ROLE")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(setterPrefix = "with")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;
    @Column(name = "ROLE_NAME")
    @Enumerated(EnumType.STRING) // to trzyma stinga
    private RoleName roleName;
//    private String roleName;
    @Column(name = "UUID")
    private UUID uuid;
    @ManyToMany(mappedBy = "roleList") //cascade = CascadeType.ALL
    private List<User> users;

}
