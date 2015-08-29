# --- !Ups

ALTER TABLE mountain_entity ADD KEY ix_mountain_entity_club_list (club_list);

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE ascent_entity DROP KEY ix_mountain_entity_club_list;

SET FOREIGN_KEY_CHECKS=1;

