package org.example.authservice2.commons.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotNull
    private String email;

    @NotNull
    private String password;

}
