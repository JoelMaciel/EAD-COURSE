package com.ead.course.api.dtos.response;

import com.ead.course.domain.models.Module;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleDTO extends RepresentationModel<ModuleDTO> {

    private UUID moduleId;
    private String title;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    public static ModuleDTO toDTO(Module module) {
        ModuleDTO moduleDTO = ModuleDTO.builder()
                .moduleId(module.getModuleId())
                .title(module.getTitle())
                .description(module.getDescription())
                .creationDate(module.getCreationDate())
                .build();

        moduleDTO.add(module.getLinks());
        return moduleDTO;
    }
}
