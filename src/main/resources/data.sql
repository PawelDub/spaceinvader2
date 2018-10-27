-- create database login;
-- use login;
drop table if exists user;
drop table if exists role;
drop table if exists user_role;
drop table if exists game_result;
create table if not exists user(user_id int primary key,
                                email varchar2(255) unique,
                                password varchar2(255),
                                name varchar2(255),
                                last_name varchar2(255),
                                active int);

create table if not exists role(
  role_id int primary key,
  role varchar2(255)
);

create table if not exists user_role(
  user_id int,
  role_id int
);

alter table USER_ROLE
  add constraint USER_ROLE_FK2 foreign key (USER_ID)
references user (USER_ID);

alter table USER_ROLE
  add constraint USER_ROLE_FK3 foreign key (role_ID)
references role (role_ID);

insert into role(role_id, role)  values (1, 'ADMIN');
insert into role(role_id, role)  values (2, 'USER');

insert into user(user_id, email, password, name, last_name, active) values(0, 'admin@admin.pl', '$2a$10$b8j83ZnPHfCc3/QbadBKz.UyAORqmeodZKQgp9vhvoBhaUU/mJ6mO', 'admin_paw', 'admin_dady', true);

insert into user_role(user_id, role_id) VALUES (0, 1);

create table if not exists game_result(id int primary key,
  user_email varchar2(255), result_time time(8) not null , result int not null );

alter table game_result
  add constraint game_FK1 foreign key (user_email)
references user (email);

commit;


