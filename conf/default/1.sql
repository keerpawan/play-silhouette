-- noinspection SqlNoDataSourceInspectionForFile
# --- !Ups

create table `user` (`userID` VARCHAR (255) NOT NULL PRIMARY KEY,`firstName` VARCHAR (255),`lastName` VARCHAR (255),`fullName` VARCHAR (255),`email` VARCHAR (255),`avatarURL` VARCHAR (255), `activated` BOOLEAN);
create table `logininfo` (`id` BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,`providerID` VARCHAR (255) NOT NULL,`providerKey` VARCHAR (255) NOT NULL);
create table `userlogininfo` (`userID` VARCHAR (255) NOT NULL,`loginInfoId` BIGINT NOT NULL);
create table `passwordinfo` (`hasher` VARCHAR (255) NOT NULL,`password` VARCHAR (255) NOT NULL,`salt` VARCHAR (255),`loginInfoId` BIGINT NOT NULL);
create table `oauth1info` (`id` BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,`token` VARCHAR (255) NOT NULL,`secret` VARCHAR (255) NOT NULL,`loginInfoId` BIGINT NOT NULL);
create table `oauth2info` (`id` BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,`accesstoken` VARCHAR (255) NOT NULL,`tokentype` VARCHAR (255),`expiresin` INTEGER,`refreshtoken` VARCHAR (255),`logininfoid` BIGINT NOT NULL);
create table `openidinfo` (`id` VARCHAR (255) NOT NULL PRIMARY KEY,`logininfoid` BIGINT NOT NULL);
create table `openidattributes` (`id` VARCHAR (255) NOT NULL,`key` VARCHAR (255) NOT NULL,`value` VARCHAR (255) NOT NULL);

# --- User permission tables

create table `role` (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, `role` VARCHAR (255) NOT NULL,
 `displayorder` INT NOT NULL,
 CONSTRAINT uroles UNIQUE (`role`));

create table `permission` (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, `permission` VARCHAR (255) NOT NULL, `roleid` INT NOT NULL,
 CONSTRAINT fk_Permission_Role FOREIGN KEY (`roleid`) REFERENCES `role`(`id`),
 CONSTRAINT rpermission UNIQUE (`permission`, `roleid`));


create table `userrole` (`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY, `userid` VARCHAR (255) NOT NULL, `roleid` INT NOT NULL,
 CONSTRAINT fk_Userrole_Role FOREIGN KEY (`roleid`) REFERENCES `role`(`id`),
 CONSTRAINT fk_Userrole_User FOREIGN KEY (`userid`) REFERENCES `user`(`userID`),
 CONSTRAINT uroles UNIQUE (`userid`, `roleid`));

# --- !Downs

drop table `openidinfo`;
drop table `oauth2info`;
drop table `oauth1info`;
drop table `passwordinfo`;
drop table `userlogininfo`;
drop table `logininfo`;
drop table `user`;