/*
 * Table PMSIInsertionInvalidation, permet de garder la trace des PMSI invalid�s
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
 * Liste l'�tat d'invalidation des fichiers pmsi ins�r�s
 * � une date donn�e
 */
CREATE OR REPLACE FUNCTION F_PMSIInsertion_Invalidation(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
-- Liste des insertions de pmsi avec un indicateur pour savoir si il a �t� invalid�
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
 * Liste les RSF actifs (derniers ins�r�s non invalid�s)
 */
CREATE OR REPLACE FUNCTION F_RSFHeader_Actifs(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT F.rsfheaderid
FROM rsfheader F
INNER JOIN (-- s�lection du fichier pmsi ins�r� en dernier pour les rsf, group� par finess et par date de fin
  SELECT
    MAX(INS.pmsiinsertionid) AS pmsiinsertionid
  FROM rsfheader F
  INNER JOIN (-- s�lection des pmsiinsertionid qui ne sont pas invalid�s � la date donn�e
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
 * Liste les rsf valides � une date donn�e
 */
CREATE OR REPLACE FUNCTION F_RSFHeader_Valides(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT F.rsfheaderid
FROM rsfheader F
INNER JOIN (-- s�lection de l'�tat d'invalidation des fichiers PMSI � une date donn�e
  SELECT INS.pmsiinsertionid
  FROM F_PMSIInsertion_Invalidation(CAST($1 AS TIMESTAMP)) AS
    INS (pmsiinsertionid BIGINT, invalide BOOLEAN)
  WHERE INS.invalide = false) INS ON
    F.pmsiinsertionid = INS.pmsiinsertionid
$BODY$
  LANGUAGE 'sql' VOLATILE;

/*
 * Fonction F_RSFHeader :
 * Liste des rsf existants � une date donn�e
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
 * Liste les RSS actifs (derniers ins�r�s non invalid�s)
 */
CREATE OR REPLACE FUNCTION F_RSSHeader_Actifs(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT F.rssheaderid
FROM rssheader F
INNER JOIN (-- s�lection du fichier pmsi ins�r� en dernier pour les rss, group� par finess et par date de fin
  SELECT
    MAX(INS.pmsiinsertionid) AS pmsiinsertionid
  FROM rssheader F
  INNER JOIN (-- s�lection des pmsiinsertionid qui ne sont pas invalid�s � la date donn�e
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
 * Liste les rss valides � une date donn�e
 */
CREATE OR REPLACE FUNCTION F_RSSHeader_Valides(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT F.rssheaderid
FROM rssheader F
INNER JOIN (-- s�lection de l'�tat d'invalidation des fichiers PMSI � une date donn�e
  SELECT INS.pmsiinsertionid
  FROM F_PMSIInsertion_Invalidation(CAST($1 AS TIMESTAMP)) AS
    INS (pmsiinsertionid BIGINT, invalide BOOLEAN)
  WHERE INS.invalide = false) INS ON
    F.pmsiinsertionid = INS.pmsiinsertionid
$BODY$
  LANGUAGE 'sql' VOLATILE;

/*
 * Fonction F_RSSHeader :
 * Liste des rss existants � une date donn�e
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
  