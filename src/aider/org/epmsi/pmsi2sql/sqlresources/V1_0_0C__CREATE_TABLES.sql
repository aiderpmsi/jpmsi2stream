-- Table pmsiinsertion
CREATE TABLE pmsiinsertion (
  pmsiinsertionid BIGINT NOT NULL DEFAULT nextval('pmsiinsertion_pmsiinsertionid_seq'),
  dateajout TIMESTAMP WITH TIME ZONE,
  nomfichier TEXT NOT NULL,
  fichier BYTEA NOT NULL,
  CONSTRAINT pmsiinsertion_pkey PRIMARY KEY (pmsiinsertionid)
);

-- Table pmsiinsertionresult
CREATE TABLE pmsiinsertionresult (
  pmsiinsertionid BIGINT NOT NULL DEFAULT currval('pmsiinsertion_pmsiinsertionid_seq'),
  status NUMERIC(1,0) NOT NULL,
  log TEXT NOT NULL,
  CONSTRAINT pmsiinsertionresult_pkey PRIMARY KEY (pmsiinsertionid)
);

-- DROP TABLE rsfheader;

CREATE TABLE rsfheader
(
  rsfheaderid bigserial NOT NULL,
  pmsiinsertionid bigint,
  line integer,
  finess numeric(9,0),
  numlot numeric(3,0),
  statutjuridique character varying(2),
  modetarifs character varying(2),
  datedebut date,
  datefin date,
  nbenregistrements numeric(6,0),
  nbrss numeric(6,0),
  premierrss numeric(7,0),
  dernierrss numeric(7,0),
  dernierenvoi character(7),
  CONSTRAINT rsfheader_pkey PRIMARY KEY (rsfheaderid ),
  CONSTRAINT rsfheader_pmsiinsertionid_fk FOREIGN KEY (pmsiinsertionid)
      REFERENCES pmsiinsertion (pmsiinsertionid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION DEFERRABLE INITIALLY DEFERRED
)
