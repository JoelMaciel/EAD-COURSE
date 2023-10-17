package com.ead.course.api.dtos.request;

import com.ead.course.domain.models.Module;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    public static Module toEntity(ModuleRequest request) {
        return Module.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();
    }
}