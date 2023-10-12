package com.ead.course.domain.models;

import com.ead.course.domain.enums.CourseLevel;
import com.ead.course.domain.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;


    @Type(type = "uuid-char")
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private UUID courseId;
    private String name;
    private String description;
    private String imageUrl;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime creationDate;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updateDate;

    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @Type(type = "uuid-char")
    private UUID userInstructor;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private Set<Module> modules;

}
