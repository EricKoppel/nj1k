# --- !Ups
ALTER TABLE range_entity MODIFY COLUMN last_update datetime DEFAULT NULL;
ALTER TABLE range_entity MODIFY COLUMN name varchar(255) NOT NULL;
ALTER TABLE range_entity ADD UNIQUE INDEX ix_range_entity_name (name);

INSERT INTO range_entity(name) VALUES('Kittatinny Mountains');
INSERT INTO range_entity(name) VALUES('Highlands');
INSERT INTO range_entity(name, parent_range) SELECT 'Wyanokies', id FROM range_entity WHERE name='Highlands';
INSERT INTO range_entity(name, parent_range) SELECT 'Northern Highlands', id FROM range_entity WHERE name='Highlands';
INSERT INTO range_entity(name, parent_range) SELECT 'Southern Highlands', id FROM range_entity WHERE name='Highlands';

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE range_entity DROP INDEX ix_range_entity_name;
TRUNCATE TABLE range_entity;

SET FOREIGN_KEY_CHECKS=1;
