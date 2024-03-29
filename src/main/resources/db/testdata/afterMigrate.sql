SET foreign_key_checks = 0;

DELETE FROM lesson;
DELETE FROM module;
DELETE FROM course_user;
DELETE FROM course;
DELETE FROM user;

SET foreign_key_checks = 1;

-- Inserting Courses
INSERT INTO course (course_id, name, description, course_status, course_level, user_instructor)
VALUES
('70754308-6ba1-469c-8de8-c3e7e28dc404', 'Java Basics', 'Introduction to Java programming', 'INPROGRESS', 'BEGINNER', '12345678-1234-1234-1234-1234567890ab'),
('9e9deb7c-6763-11ee-8c99-0242ac120002', 'Advanced Java', 'Deep dive into Java', 'INPROGRESS', 'ADVANCED', '12345678-1234-1234-1234-1234567890ab');

-- Inserting Users
INSERT INTO `user` (
  `user_id`, `username`, `email`, `full_name`, `cpf`, `phone_number`,
  `user_status`, `user_type`, `image_url`
) VALUES
  (
    '3106c73c-5142-480b-8344-388610678971', 'johnDoe', 'john.doe@example.com', 'John Doe',
    '123456', '(555) 123-4567', 'ACTIVE', 'STUDENT', 'http://example.com/image1.jpg'
  ),
  (
    '99735306-994d-46f9-82a7-4116145a5678', 'aliceSmith', 'alice.smith@example.com', 'Alice Smith',
    '38580865093', '(11) 98765-4321', 'BLOCKED', 'INSTRUCTOR', 'http://example.com/image2.jpg'
  ),
  (
    'c85685f2-4135-492d-873c-936471e60792', 'bobJones', 'bob.jones@example.com', 'Bob Jones',
    '59311079081', '020 7946 0852', 'ACTIVE', 'ADMIN', 'http://example.com/image3.jpg'
  );;


-- Inserting Modules
INSERT INTO module (module_id, title, description, course_id)
VALUES
('0e7f73ed-38ed-4050-ac8d-de0195e60862', 'Variables and Data Types', 'Deep dive into Java variables', '70754308-6ba1-469c-8de8-c3e7e28dc404'),
('7a2ec76c-4d7c-463a-836a-18a55b5272df', 'Java Streams', 'Deep dive into Java streams', '9e9deb7c-6763-11ee-8c99-0242ac120002');


-- Inserting Lessons
INSERT INTO lesson (lesson_id, title, description, video_url, module_id)
VALUES
('c9a8ed0a-423d-4e99-ac53-dcc5174113a1', 'Java Overview', 'A brief overview of Java', 'http://example.com/video1.mp4', '0e7f73ed-38ed-4050-ac8d-de0195e60862'),
('adcbb827-223c-496a-9e7d-5eaa13ccc725', 'Java History', 'How Java evolved over time', 'http://example.com/video2.mp4', '7a2ec76c-4d7c-463a-836a-18a55b5272df');


INSERT INTO `course_user` (`id`, `course_id`, `user_id`) VALUES
  ('f48be9d0-714e-442d-9fdf-fe67145e0a06', '70754308-6ba1-469c-8de8-c3e7e28dc404', '3106c73c-5142-480b-8344-388610678971'),
  ('c9805b25-ab2f-404b-a3a4-e518656d131e', '9e9deb7c-6763-11ee-8c99-0242ac120002', '99735306-994d-46f9-82a7-4116145a5678');

