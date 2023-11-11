ALTER TABLE `course_user`
ADD CONSTRAINT `fk_course_user_user_id`
FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE;
