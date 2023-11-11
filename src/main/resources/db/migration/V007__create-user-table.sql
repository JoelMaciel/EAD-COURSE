CREATE TABLE `user` (
    `user_id` CHAR(36) NOT NULL PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `email` VARCHAR(50) NOT NULL UNIQUE,
    `full_name` VARCHAR(150) NOT NULL ,
    `cpf` VARCHAR(11) NOT NULL UNIQUE,
    `user_status` ENUM('ACTIVE', 'BLOCKED') NOT NULL,
    `user_type` ENUM('ADMIN', 'STUDENT', 'INSTRUCTOR') NOT NULL,
    `image_url` VARCHAR(255) NULL

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
