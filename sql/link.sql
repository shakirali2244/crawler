-- Table: link

-- DROP TABLE link;

CREATE TABLE link
(
  pid integer REFERENCES domain (id),
  cid integer REFERENCES domain (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE link
  OWNER TO postgres;
