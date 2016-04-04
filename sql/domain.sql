-- Table: domain

-- DROP TABLE domain;

CREATE TABLE domain
(
  id serial NOT NULL,
  hostname character varying,
  page character varying,
 bk_link REFERENCES domain(id),  CONSTRAINT domain_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE domain
  OWNER TO postgres;
