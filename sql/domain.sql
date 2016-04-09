-- Table: domain

-- DROP TABLE domain;

CREATE TABLE domain
(
  id serial NOT NULL PRIMARY KEY,
  hostname character varying,
  page character varying
)
WITH (
  OIDS=FALSE
);
ALTER TABLE domain
  OWNER TO postgres;
