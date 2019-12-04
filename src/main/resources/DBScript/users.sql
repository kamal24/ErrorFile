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

create table user_role(
    userid INT unsigned not null,
    roleid INT unsigned not null,
    FOREIGN KEY (userid)
            REFERENCES user(userid),
    FOREIGN KEY (roleid)
                REFERENCES role(roleid)
);

show tables;

