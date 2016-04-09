-- Table: meta

-- DROP TABLE meta;

CREATE TABLE meta
(
  id serial NOT NULL PRIMARY KEY,
  keywords character varying,
  "desc" character varying,
  did integer REFERENCES domain(id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE meta
  OWNER TO postgres;
