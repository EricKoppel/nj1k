# --- !Ups

CREATE TABLE range_entity (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(255) DEFAULT NULL,
  parent_range bigint(20) DEFAULT NULL,
  last_update datetime NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_range_entity FOREIGN KEY (parent_range) REFERENCES range_entity (id)
);

ALTER TABLE mountain_entity ADD COLUMN range_id BIGINT(20), ADD CONSTRAINT fk_range_id FOREIGN KEY fk_range_id(range_id) REFERENCES range_entity (id) ON DELETE RESTRICT ON UPDATE RESTRICT;


# --- !Downs

DROP TABLE range_entity;
ALTER TABLE mountain_entity DROP COLUMN range_id;
ALTER TABLE mountain_entity DROP CONSTRAINT fk_range_id;
