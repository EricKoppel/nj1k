# --- !Ups

ALTER TABLE range_entity ADD COLUMN description LONGTEXT;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE range_entity DROP COLUMN description;

SET FOREIGN_KEY_CHECKS=1;

