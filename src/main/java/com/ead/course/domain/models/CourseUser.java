package com.ead.course.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course_user")
public class CourseUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Type(type = "uuid-char")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

}
