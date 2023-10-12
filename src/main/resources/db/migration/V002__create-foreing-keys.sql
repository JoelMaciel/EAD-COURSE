ALTER TABLE module
ADD COLUMN course_id CHAR(36) NOT NULL,
ADD FOREIGN KEY (course_id) REFERENCES course(course_id);

ALTER TABLE lesson
ADD COLUMN module_id CHAR(36) NOT NULL,
ADD FOREIGN KEY (module_id) REFERENCES module(module_id);
