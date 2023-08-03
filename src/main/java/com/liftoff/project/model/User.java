package com.liftoff.project.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "APP_USER")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(setterPrefix = "with")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true)
    private Long id;

    @Column(name = "FIRST_NAME")
    @NotBlank(message="User's first name cannot be empty.")
    private String firstName;
    @Column(name = "LAST_NAME")
   @NotBlank(message = "User's sure name cannot be empty.")
    private String lastName;
    @Column(name = "EMAIL")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email must be valid like name@domain.pl")
   @NotBlank(message = "User's email cannot be empty.")
    private String email;
    @Column(name = "PASSWORD")
    @NotBlank(message = "User's password cannot be empty.")
    private String password;
    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "CREATED_AT")
    private Instant createdAt;
    @UpdateTimestamp(source = SourceType.DB)
    @Column(name = "UPDATED_AT")
    private Instant updatedAt;
    @Column(name = "IS_ENABLED")
    private Integer isEnabled;
    @Column(name = "UUID")
    private UUID uuid;
//    @JsonIgnore
//    @ManyToMany(fetch = FetchType.EAGER) //cascade = CascadeType.ALL
//    @JoinTable(
//            name = "APP_USER_ROLE",
//            joinColumns = @JoinColumn(name = "ID_APP_USER"),
//            inverseJoinColumns = @JoinColumn(name = "ID_ROLE"))

    @Enumerated(EnumType.STRING) // to trzyma stinga
    @Column(name = "ROLE")
    private Role role;

    public boolean isEnabled() {

        boolean isEnabled = false;
        if(this.isEnabled==1){isEnabled = true;}
          return isEnabled;

    }

//    @Override
//    public String toString() {
//
//        String roles = this.roleList.stream().map(role-> role.name().toString()).collect(Collectors.joining(","));
//        return "UserArch{" +
//                "id=" + id +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", createdAt=" + createdAt +
//                ", updatedAt=" + updatedAt +
//                ", isEnabled=" + isEnabled() +
//                ", uuid=" + uuid.toString() +
//                ", role=" + roles +
//                '}';
//    }


}
