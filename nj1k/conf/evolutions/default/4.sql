# --- !Ups

ALTER TABLE ascent_entity ADD KEY ix_ascent_entity_ascent_date_climber_id (ascent_date,climber_id) USING BTREE;
ALTER TABLE ascent_entity ADD KEY ix_ascent_entity_climber_id_mountain_id (climber_id,mountain_id) USING BTREE;
ALTER TABLE ascent_entity ADD KEY ix_ascent_entity_successful (successful) USING BTREE;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE ascent_entity DROP KEY ix_ascent_entity_ascent_date_climber_id;
ALTER TABLE ascent_entity DROP KEY ix_ascent_entity_climber_id_mountain_id;
ALTER TABLE ascent_entity DROP KEY ix_ascent_entity_successful;

SET FOREIGN_KEY_CHECKS=1;

