# --- !Ups

ALTER TABLE mountain_entity DROP COLUMN mountainrange;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE mountain_entity ADD COLUMN mountainrange VARCHAR(255);

SET FOREIGN_KEY_CHECKS=1;

