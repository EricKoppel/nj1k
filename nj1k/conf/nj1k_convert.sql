INSERT INTO nj1knew.news_entity(`id`,`news_date`,`title`,`entry`)
SELECT `id`,FROM_UNIXTIME(`date`),`title`,`entry` FROM nj1k.news;

INSERT INTO nj1knew.user_entity(`id`,email,password,name,aboutme)
SELECT `id`,email,password,name,aboutme FROM nj1k.users;

INSERT INTO nj1knew.maps_entity (`id`,name,publisher)
SELECT `id`,name,publisher FROM nj1k.maps;

INSERT INTO nj1knew.mountain_entity (`id`,name,county,latitude,longitude,elevation,owner,ownership,description,mountainrange,name_type,trueoneker,club_list)
SELECT `id`,name,county,lat,`long`,elevation,owner,ownership,description,`range`,name_type,`true_1ker`,club_list FROM nj1k.summits;

INSERT INTO nj1knew.ascent_entity (`id`,`ascent_date`,successful,trip_report,climber_id,mountain_id)
SELECT `id`,FROM_UNIXTIME(`date`),successful,trip_report,climber_id,mountain_id
FROM nj1k.ascent_list 
WHERE mountain_id IN (SELECT `id` FROM nj1k.`summits`)
AND climber_id IN (SELECT `id` FROM nj1k.`users`);

INSERT INTO nj1knew.role_entity (`role_name`) VALUES ('user');
INSERT INTO nj1knew.role_entity (`role_name`) VALUES ('admin');

INSERT INTO nj1knew.users_roles (`user_id`,`role_id`)
SELECT u.`id` AS user_id, r.`id` AS role_id
FROM nj1knew.user_entity u, nj1knew.role_entity r WHERE r.role_name='user';

INSERT INTO nj1knew.users_roles (`user_id`,`role_id`)
SELECT u.`id` AS user_id, r.`id` AS role_id
FROM nj1knew.user_entity u, nj1knew.role_entity r WHERE r.role_name='admin'
AND u.email='erk7@njit.edu';

INSERT INTO nj1knew.ascent_detail_entity(caption, ascent_id, descriptor)
SELECT tr.caption, tr.ascent_id, tr.filename
FROM nj1k.tr_images tr;

UPDATE nj1knew.news_entity SET last_update=NOW();
UPDATE nj1knew.user_entity SET last_update=NOW();
UPDATE nj1knew.maps_entity SET last_update=NOW();
UPDATE nj1knew.mountain_entity SET last_update=NOW();
UPDATE nj1knew.ascent_entity SET last_update=NOW();
UPDATE nj1knew.role_entity SET last_update=NOW();
UPDATE nj1knew.ascent_detail_entity SET last_update=NOW();