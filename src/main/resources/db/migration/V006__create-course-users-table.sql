CREATE TABLE `course_user` (
    `id` CHAR(36) NOT NULL PRIMARY KEY,
    `course_id` CHAR(36) NOT NULL,
    `user_id` CHAR(36) NOT NULL,
    FOREIGN KEY (`course_id`) REFERENCES `course` (`course_id`) ON DELETE CASCADE
);
