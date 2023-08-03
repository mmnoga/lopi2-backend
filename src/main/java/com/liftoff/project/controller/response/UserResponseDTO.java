package com.liftoff.project.controller.response;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Instant createdAt;

    private Instant updatedAt;

    private Integer isEnabled;

    private UUID uuid;

    private List<RoleResponseDTO> roleList;


}
