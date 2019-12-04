create database users;

show databases;

create table user(
   userid INT unsigned NOT NULL AUTO_INCREMENT,
   emailaddress VARCHAR(200) NOT NULL unique,
   name VARCHAR(200) NOT NULL,
   PRIMARY KEY (userid)
  );

create table role(
   roleid INT unsigned NOT NULL AUTO_INCREMENT,
   name VARCHAR(200) NOT NULL,
   PRIMARY KEY (roleid)
);

insert into role values(1,"SA");
insert into role values(2,"Admin");
insert into role values(3,"User");

create table user_role(
    userid INT unsigned not null,
    roleid INT unsigned not null,
    FOREIGN KEY (userid)
            REFERENCES user(userid),
    FOREIGN KEY (roleid)
                REFERENCES role(roleid)
);

show tables;

select * from user;
select * from role;
select * from user_role;

