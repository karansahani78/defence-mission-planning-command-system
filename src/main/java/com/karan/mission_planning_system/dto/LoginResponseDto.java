package com.karan.mission_planning_system.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDto {

    private String accessToken;
    private String tokenType = "Bearer";
}
