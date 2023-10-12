package com.ead.course.api.dtos.response;

import com.ead.course.domain.models.Module;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleDTO {

    private UUID moduleId;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    public static ModuleDTO toDTO(Module module) {
        return ModuleDTO.builder()
                .moduleId(module.getModuleId())
                .title(module.getTitle())
                .description(module.getDescription())
                .creationDate(module.getCreationDate())
                .build();
    }
}
