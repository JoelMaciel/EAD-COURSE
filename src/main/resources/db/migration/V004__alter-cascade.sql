ALTER TABLE module DROP FOREIGN KEY module_ibfk_1;
ALTER TABLE lesson DROP FOREIGN KEY lesson_ibfk_1;

ALTER TABLE module
ADD FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE;

ALTER TABLE lesson
ADD FOREIGN KEY (module_id) REFERENCES module(module_id) ON DELETE CASCADE;
