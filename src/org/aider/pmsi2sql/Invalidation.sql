/*
 * Table PMSIInsertionInvalidation, permet de garder la trace des PMSI invalidés
 */
CREATE TABLE PMSIInsertionInvalidation
  (PMSIInsertionInvalidationid bigserial NOT NULL,
   pmsiinsertionid bigint NOT NULL,
   DateInvalidation timestamp without time zone NOT NULL DEFAULT now(),
   Invalidation BOOLEAN NOT NULL,
   PRIMARY KEY (PMSIInsertionInvalidationid),
   FOREIGN KEY (pmsiinsertionid) REFERENCES pmsiinsertion (pmsiinsertionid)
);

/*
 * Fonction F_PMSIInsertion_Invalidation :
 * Liste l'état d'invalidation des fichiers pmsi insérés
 * à une date donnée
 */
CREATE OR REPLACE FUNCTION F_PMSIInsertion_Invalidation(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
-- Liste des insertions de pmsi avec un indicateur pour savoir si il a été invalidé
SELECT INS.pmsiinsertionid, COALESCE(INV.invalidation, false) AS Invalide
FROM pmsiinsertion INS
LEFT OUTER JOIN (
  SELECT INV.pmsiinsertionid, INV.pmsiinsertioninvalidationid, INV.invalidation
  FROM pmsiinsertioninvalidation INV
  INNER JOIN (
    SELECT INV.pmsiinsertionid, MAX(INV.pmsiinsertioninvalidationid) AS pmsiinsertioninvalidationid
    FROM pmsiinsertioninvalidation INV
    WHERE INV.dateinvalidation < $1
    GROUP BY INV.pmsiinsertionid) INV2 ON
      INV.pmsiinsertionid = INV2.pmsiinsertionid AND
      INV.pmsiinsertioninvalidationid = INV2.pmsiinsertioninvalidationid) INV ON
    INS.pmsiinsertionid = INV.pmsiinsertionid
  WHERE INS.dateajout < $1
$BODY$
  LANGUAGE 'sql' VOLATILE;

/*
 * Fonction F_RSFHeader_Actifs :
 * Liste les RSF actifs (derniers insérés non invalidés)
 */
CREATE OR REPLACE FUNCTION F_RSFHeader_Actifs(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT F.rsfheaderid
FROM rsfheader F
INNER JOIN (-- sélection du fichier pmsi inséré en dernier pour les rsf, groupé par finess et par date de fin
  SELECT
    MAX(INS.pmsiinsertionid) AS pmsiinsertionid
  FROM rsfheader F
  INNER JOIN (-- sélection des pmsiinsertionid qui ne sont pas invalidés à la date donnée
    SELECT INS.pmsiinsertionid
    FROM F_PMSIInsertion_Invalidation(CAST($1 AS TIMESTAMP)) AS
      INS (pmsiinsertionid BIGINT, invalide BOOLEAN)
    WHERE INS.invalide = false) INS ON
      F.pmsiinsertionid = INS.pmsiinsertionid
  GROUP BY F.finess, F.datefin) INS ON
    F.pmsiinsertionid = INS.pmsiinsertionid
$BODY$
  LANGUAGE 'sql' VOLATILE;

/*
 * Fonction F_RSFHeader_Valides :
 * Liste les rsf valides à une date donnée
 */
CREATE OR REPLACE FUNCTION F_RSFHeader_Valides(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT F.rsfheaderid
FROM rsfheader F
INNER JOIN (-- sélection de l'état d'invalidation des fichiers PMSI à une date donnée
  SELECT INS.pmsiinsertionid
  FROM F_PMSIInsertion_Invalidation(CAST($1 AS TIMESTAMP)) AS
    INS (pmsiinsertionid BIGINT, invalide BOOLEAN)
  WHERE INS.invalide = false) INS ON
    F.pmsiinsertionid = INS.pmsiinsertionid
$BODY$
  LANGUAGE 'sql' VOLATILE;

/*
 * Fonction F_RSFHeader :
 * Liste des rsf existants à une date donnée
 */
CREATE OR REPLACE FUNCTION F_RSFHeader(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT F.rsfheaderid
FROM rsfheader F
INNER JOIN pmsiinsertion INS ON
  F.pmsiinsertionid = INS.pmsiinsertionid
WHERE INS.dateajout < $1
$BODY$
  LANGUAGE 'sql' VOLATILE;

/*
 * Fonction F_RSSHeader_Actifs :
 * Liste les RSS actifs (derniers insérés non invalidés)
 */
CREATE OR REPLACE FUNCTION F_RSSHeader_Actifs(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT F.rssheaderid
FROM rssheader F
INNER JOIN (-- sélection du fichier pmsi inséré en dernier pour les rss, groupé par finess et par date de fin
  SELECT
    MAX(INS.pmsiinsertionid) AS pmsiinsertionid
  FROM rssheader F
  INNER JOIN (-- sélection des pmsiinsertionid qui ne sont pas invalidés à la date donnée
    SELECT INS.pmsiinsertionid
    FROM F_PMSIInsertion_Invalidation(CAST($1 AS TIMESTAMP)) AS
      INS (pmsiinsertionid BIGINT, invalide BOOLEAN)
    WHERE INS.invalide = false) INS ON
      F.pmsiinsertionid = INS.pmsiinsertionid
  GROUP BY F.finess, F.finperiode) INS ON
    F.pmsiinsertionid = INS.pmsiinsertionid
$BODY$
  LANGUAGE 'sql' VOLATILE;

/*
 * Fonction F_RSSHeader_Valides :
 * Liste les rss valides à une date donnée
 */
CREATE OR REPLACE FUNCTION F_RSSHeader_Valides(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT F.rssheaderid
FROM rssheader F
INNER JOIN (-- sélection de l'état d'invalidation des fichiers PMSI à une date donnée
  SELECT INS.pmsiinsertionid
  FROM F_PMSIInsertion_Invalidation(CAST($1 AS TIMESTAMP)) AS
    INS (pmsiinsertionid BIGINT, invalide BOOLEAN)
  WHERE INS.invalide = false) INS ON
    F.pmsiinsertionid = INS.pmsiinsertionid
$BODY$
  LANGUAGE 'sql' VOLATILE;

/*
 * Fonction F_RSSHeader :
 * Liste des rss existants à une date donnée
 */
CREATE OR REPLACE FUNCTION F_RSSHeader(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT F.rssheaderid
FROM rssheader F
INNER JOIN pmsiinsertion INS ON
  F.pmsiinsertionid = INS.pmsiinsertionid
WHERE INS.dateajout < $1
$BODY$
  LANGUAGE 'sql' VOLATILE;
  