CREATE TABLE course (
    course_id CHAR(36) NOT NULL,
    name VARCHAR(150) NOT NULL UNIQUE,
    description VARCHAR(250) NOT NULL,
    image_url VARCHAR(500),
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    course_status ENUM('INPROGRESS', 'CONCLUDED') NOT NULL,
    course_level ENUM('BEGINNER', 'INTERMEDIARY', 'ADVANCED') NOT NULL,
    user_instructor CHAR(36) NOT NULL,
    PRIMARY KEY (course_id)
);

CREATE TABLE module (
    module_id CHAR(36) NOT NULL,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(250) NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (module_id)
);

CREATE TABLE lesson (
    lesson_id CHAR(36) NOT NULL,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(250) NOT NULL,
    video_url VARCHAR(250) NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (lesson_id)
);
