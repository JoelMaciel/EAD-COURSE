SET foreign_key_checks = 0;

DELETE FROM lesson;
DELETE FROM module;
DELETE FROM course;

SET foreign_key_checks = 1;

-- Inserting Courses
INSERT INTO course (course_id, name, description, course_status, course_level, user_instructor)
VALUES
('70754308-6ba1-469c-8de8-c3e7e28dc404', 'Java Basics', 'Introduction to Java programming', 'INPROGRESS', 'BEGINNER', '12345678-1234-1234-1234-1234567890ab'),
('9e9deb7c-6763-11ee-8c99-0242ac120002', 'Advanced Java', 'Deep dive into Java', 'INPROGRESS', 'ADVANCED', '12345678-1234-1234-1234-1234567890ab');

-- Inserting Modules
INSERT INTO module (module_id, title, description, course_id)
VALUES
('0e7f73ed-38ed-4050-ac8d-de0195e60862', 'Variables and Data Types', 'Deep dive into Java variables', '70754308-6ba1-469c-8de8-c3e7e28dc404'),
('eb3b9fbe-b01a-4cdb-90e4-ce12d5feb89f', 'Java Streams', 'Deep dive into Java streams', '9e9deb7c-6763-11ee-8c99-0242ac120002');

-- Inserting Lessons
INSERT INTO lesson (lesson_id, title, description, video_url, module_id)
VALUES
('c9a8ed0a-423d-4e99-ac53-dcc5174113a1', 'Java Overview', 'A brief overview of Java', 'http://example.com/video1.mp4', '0e7f73ed-38ed-4050-ac8d-de0195e60862'),
('13095a2-678f-11ee-8c99-0242ac120002', 'Java History', 'How Java evolved over time', 'http://example.com/video2.mp4', 'eb3b9fbe-b01a-4cdb-90e4-ce12d5feb89f');
