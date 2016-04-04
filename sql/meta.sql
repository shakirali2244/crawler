-- Table: meta

-- DROP TABLE meta;

CREATE TABLE meta
(
  id serial NOT NULL,
  keywords character varying,
  "desc" character varying,
  did integer,
  CONSTRAINT meta_did_fkey FOREIGN KEY (did)
      REFERENCES domain (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE meta
  OWNER TO postgres;
