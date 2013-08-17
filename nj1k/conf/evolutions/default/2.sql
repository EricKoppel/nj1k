# --- !Ups
ALTER TABLE ascent_detail_entity ADD descriptor varchar(255);

# --- !Downs
ALTER TABLE ascent_detail_entity DROP descriptor;