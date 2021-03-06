# --- !Ups

create table ascent_detail_entity (
id                        bigint auto_increment not null,
image                     longblob,
caption                   longtext,
ascent_id                 bigint,
descriptor                varchar(255),
thumbnail                 longblob,
last_update               datetime not null,
constraint pk_ascent_detail_entity primary key (id))
;

create table ascent_entity (
id                        bigint auto_increment not null,
ascent_date               datetime,
successful                tinyint(1) default 0,
trip_report               longtext,
climber_id                bigint,
mountain_id               bigint,
last_update               datetime not null,
constraint pk_ascent_entity primary key (id))
;

create table maps_entity (
id                        bigint auto_increment not null,
name                      varchar(255),
publisher                 varchar(255),
last_update               datetime not null,
constraint pk_maps_entity primary key (id))
;

create table mountain_entity (
id                        bigint auto_increment not null,
name                      varchar(255),
county                    varchar(255),
latitude                  float,
longitude                 float,
elevation                 integer,
owner                     varchar(255),
ownership                 varchar(255),
picture                   longblob,
description               longtext,
mountainrange             varchar(255),
name_type                 varchar(10),
trueoneker                tinyint(1) default 0,
club_list                 tinyint(1) default 0,
last_update               datetime not null,
constraint ck_mountain_entity_name_type check (name_type in ('unofficial','official')),
constraint pk_mountain_entity primary key (id))
;

create table news_entity (
id                        bigint auto_increment not null,
news_date                 datetime,
title                     varchar(255),
entry                     longtext,
last_update               datetime not null,
constraint pk_news_entity primary key (id))
;

create table news_image_entity (
id                        bigint auto_increment not null,
image                     longblob,
caption                   longtext,
news_id                   bigint,
last_update               datetime not null,
constraint pk_news_image_entity primary key (id))
;

create table role_entity (
id                        bigint auto_increment not null,
role_name                 varchar(255),
last_update               datetime not null,
constraint uq_role_entity_role_name unique (role_name),
constraint pk_role_entity primary key (id))
;

create table user_aggregate (
user_id                   bigint)
;

create table user_entity (
id                        bigint auto_increment not null,
email                     varchar(255),
password                  varchar(255),
salt                      varchar(255),
name                      varchar(255),
aboutme                   longtext,
pic                       longblob,
last_update               datetime not null,
constraint uq_user_entity_email unique (email),
constraint pk_user_entity primary key (id))
;


create table maps_entity_mountain_entity (
maps_entity_id                 bigint not null,
mountain_entity_id             bigint not null,
constraint pk_maps_entity_mountain_entity primary key (maps_entity_id, mountain_entity_id))
;

create table mountain_entity_maps_entity (
mountain_entity_id             bigint not null,
maps_entity_id                 bigint not null,
constraint pk_mountain_entity_maps_entity primary key (mountain_entity_id, maps_entity_id))
;

create table users_roles (
user_id                        bigint not null,
role_id                        bigint not null,
constraint pk_users_roles primary key (user_id, role_id))
;
alter table ascent_detail_entity add constraint fk_ascent_detail_entity_ascent_1 foreign key (ascent_id) references ascent_entity (id) on delete restrict on update restrict;
create index ix_ascent_detail_entity_ascent_1 on ascent_detail_entity (ascent_id);
alter table ascent_entity add constraint fk_ascent_entity_climber_2 foreign key (climber_id) references user_entity (id) on delete restrict on update restrict;
create index ix_ascent_entity_climber_2 on ascent_entity (climber_id);
alter table ascent_entity add constraint fk_ascent_entity_mountain_3 foreign key (mountain_id) references mountain_entity (id) on delete restrict on update restrict;
create index ix_ascent_entity_mountain_3 on ascent_entity (mountain_id);
alter table news_image_entity add constraint fk_news_image_entity_news_4 foreign key (news_id) references news_entity (id) on delete restrict on update restrict;
create index ix_news_image_entity_news_4 on news_image_entity (news_id);
alter table user_aggregate add constraint fk_user_aggregate_user_5 foreign key (user_id) references user_entity (id) on delete restrict on update restrict;
create index ix_user_aggregate_user_5 on user_aggregate (user_id);



alter table maps_entity_mountain_entity add constraint fk_maps_entity_mountain_entity_maps_entity_01 foreign key (maps_entity_id) references maps_entity (id) on delete restrict on update restrict;

alter table maps_entity_mountain_entity add constraint fk_maps_entity_mountain_entity_mountain_entity_02 foreign key (mountain_entity_id) references mountain_entity (id) on delete restrict on update restrict;

alter table mountain_entity_maps_entity add constraint fk_mountain_entity_maps_entity_mountain_entity_01 foreign key (mountain_entity_id) references mountain_entity (id) on delete restrict on update restrict;

alter table mountain_entity_maps_entity add constraint fk_mountain_entity_maps_entity_maps_entity_02 foreign key (maps_entity_id) references maps_entity (id) on delete restrict on update restrict;

alter table users_roles add constraint fk_users_roles_user_entity_01 foreign key (user_id) references user_entity (id) on delete restrict on update restrict;

alter table users_roles add constraint fk_users_roles_role_entity_02 foreign key (role_id) references role_entity (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table ascent_detail_entity;

drop table ascent_entity;

drop table maps_entity;

drop table maps_entity_mountain_entity;

drop table mountain_entity;

drop table mountain_entity_maps_entity;

drop table news_entity;

drop table news_image_entity;

drop table role_entity;

drop table users_roles;

drop table user_aggregate;

drop table user_entity;

SET FOREIGN_KEY_CHECKS=1;
