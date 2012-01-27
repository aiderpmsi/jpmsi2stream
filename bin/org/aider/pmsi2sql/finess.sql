CREATE TABLE  FINESS (
  idFINESS BIGSERIAL,
  FINESS NUMERIC(9) NOT NULL,
  NomEtablissement VARCHAR(80) NOT NULL,
  PRIMARY KEY (idFINESS),
  UNIQUE (FINESS)
);

INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('340780600', 'montpellier');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('340000264', 'juridique');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('660005216', 'uad perpignan');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('660005208', 'uad le boulou');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('660005190', 'uad font romeu');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('660005182', 'uad elne');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('480001783', 'marvejols');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('480001403', 'uad mende');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('340013259', 'uad bedarieux');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('340013499', 'uad beziers');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('340013218', 'uad ganges');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('340013408', 'uad sete');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('340013358', 'uad bouzigues');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('300787421', 'nimes');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('340013309', 'uad clermont');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('340013119', 'uad grabels');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('300007168', 'uad bagnols');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('300007119', 'ales');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('110004439', 'uad trebes');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('110004421', 'uad limoux');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('110004413', 'uad narbonne');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('110005311', 'carcassonne');
INSERT INTO FINESS (FINESS, NomEtablissement) VALUES ('120787254', 'millau');