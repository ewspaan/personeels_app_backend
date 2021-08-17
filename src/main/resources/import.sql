
INSERT INTO role(name) VALUES('ROLE_USER');
INSERT INTO role(name) VALUES('ROLE_ADMIN');
INSERT INTO role(name) VALUES('ROLE_MODERATOR');

INSERT INTO companies (company_name) VALUES ('De Blaaters');

INSERT INTO app_users (date_of_birth,email,first_name,last_name,password,phone_number,username, company_id)VALUES ('01-01-2000','bla@bla.com','Bla','MacBla','$2a$10$M849OBN0.suyAGjJzUxWaONo6Pq2JDTKmHIgJlBF4jO39HYFX8OlG','06-12345678','bla',(select id from companies where company_name = 'De Blaaters'));

INSERT INTO user_role (role_id, user_id) VALUES ((SELECT id FROM role WHERE name  = 'ROLE_MODERATOR'),(SELECT id FROM app_users where username = 'bla'));