package com.ead.course.api.dtos.event;

import com.ead.course.domain.models.User;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEventDTO {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private String userType;
    private String userStatus;
    private String cpf;
    private String imageUrl;
    private String phoneNumber;
    private String actionType;

    public static User toUser(UserEventDTO eventDTO) {
        return User.builder()
                .userId(eventDTO.getUserId())
                .username(eventDTO.getUsername())
                .email(eventDTO.getEmail())
                .fullName(eventDTO.getFullName())
                .userType(eventDTO.getUserType())
                .userStatus(eventDTO.getUserStatus())
                .cpf(eventDTO.getCpf())
                .phoneNumber(eventDTO.getPhoneNumber())
                .imageUrl(eventDTO.getImageUrl())
                .build();
    }
}
