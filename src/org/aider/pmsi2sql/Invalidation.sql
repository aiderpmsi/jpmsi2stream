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
 * Recherche pour chaque FINESS la liste des derniers rsf insérés valide.
 */
CREATE OR REPLACE VIEW V_Actual_RSFHeader
AS
SELECT MAX(F.rsfheaderid) AS rsfheaderid
  FROM RSFHeader F
  LEFT OUTER JOIN (-- Liste des RSF invalidés, permet de les éliminer
    SELECT F.rsfheaderid
    FROM rsfheader F
    INNER JOIN pmsiinsertion INS ON
        F.pmsiinsertionid = INS.pmsiinsertionid
    INNER JOIN pmsiinsertioninvalidation VAL ON
        INS.pmsiinsertionid = VAL.pmsiinsertionid
    INNER JOIN (-- Récupéraion du dernier état d'invalidation par rsf
        SELECT MAX(VAL.pmsiinsertioninvalidationid) AS pmsiinsertioninvalidationid
        FROM rsfheader F
        INNER JOIN pmsiinsertion INS ON
            F.pmsiinsertionid = INS.pmsiinsertionid
        INNER JOIN pmsiinsertioninvalidation VAL ON
            INS.pmsiinsertionid = VAL.pmsiinsertionid
        GROUP BY F.rsfheaderid) InvalEtat ON
        VAL.pmsiinsertioninvalidationid = InvalEtat.pmsiinsertioninvalidationid
    WHERE VAL.Invalidation = true) RSFInval ON
    F.rsfheaderid = RSFInval.rsfheaderid
  -- On ne garde que les RSF qui n'ont pas d'état d'invalidation
  WHERE RSFInval.rsfheaderid IS NULL
  GROUP BY F.FINESS, F.DateFin;

/*
 * Liste des rsf utilises à une date
 */
CREATE OR REPLACE FUNCTION V_Date_RSFHeader(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT MAX(F.rsfheaderid) AS rsfheaderid
  FROM RSFHeader F
  INNER JOIN pmsiinsertion INS ON
    F.pmsiinsertionid = INS.pmsiinsertionid
  LEFT OUTER JOIN (-- Liste des RSF invalidés, permet de les éliminer
    SELECT F.rsfheaderid
    FROM rsfheader F
    INNER JOIN pmsiinsertion INS ON
        F.pmsiinsertionid = INS.pmsiinsertionid
    INNER JOIN pmsiinsertioninvalidation VAL ON
        INS.pmsiinsertionid = VAL.pmsiinsertionid
    INNER JOIN (-- Récupéraion du dernier état d'invalidation par rsf
        SELECT MAX(VAL.pmsiinsertioninvalidationid) AS pmsiinsertioninvalidationid
        FROM rsfheader F
        INNER JOIN pmsiinsertion INS ON
            F.pmsiinsertionid = INS.pmsiinsertionid
        INNER JOIN pmsiinsertioninvalidation VAL ON
            INS.pmsiinsertionid = VAL.pmsiinsertionid
        WHERE INS.dateajout < $1
        GROUP BY F.rsfheaderid) InvalEtat ON
        VAL.pmsiinsertioninvalidationid = InvalEtat.pmsiinsertioninvalidationid
    WHERE VAL.Invalidation = true) RSFInval ON
    F.rsfheaderid = RSFInval.rsfheaderid
  -- On ne garde que les RSF qui n'ont pas d'état d'invalidation
  WHERE RSFInval.rsfheaderid IS NULL AND
    INS.dateajout < $1
  GROUP BY F.FINESS, F.DateFin;
$BODY$
  LANGUAGE 'sql' VOLATILE;

 /*
 * Table RSSInvalidation, permet de garder la trace des RSS invalidés
 */
CREATE OR REPLACE VIEW V_Actual_RSSHeader
AS
SELECT MAX(S.rssheaderid) AS rssheaderid
  FROM RSSHeader S
  LEFT OUTER JOIN (-- Liste des RSF invalidés, permet de les éliminer
    SELECT S.rssheaderid
    FROM rssheader S
    INNER JOIN pmsiinsertion INS ON
        S.pmsiinsertionid = INS.pmsiinsertionid
    INNER JOIN pmsiinsertioninvalidation VAL ON
        INS.pmsiinsertionid = VAL.pmsiinsertionid
    INNER JOIN (-- Récupéraion du dernier état d'invalidation par rsf
        SELECT MAX(VAL.pmsiinsertioninvalidationid) AS pmsiinsertioninvalidationid
        FROM rssheader S
        INNER JOIN pmsiinsertion INS ON
            S.pmsiinsertionid = INS.pmsiinsertionid
        INNER JOIN pmsiinsertioninvalidation VAL ON
            INS.pmsiinsertionid = VAL.pmsiinsertionid
        GROUP BY S.rssheaderid) InvalEtat ON
        VAL.pmsiinsertioninvalidationid = InvalEtat.pmsiinsertioninvalidationid
    WHERE VAL.Invalidation = true) RSSInval ON
    S.rssheaderid = RSSInval.rssheaderid
  -- On ne garde que les RSF qui n'ont pas d'état d'invalidation
  WHERE RSSInval.rssheaderid IS NULL
  GROUP BY S.FINESS, S.FinPeriode;
  

/*
 * Liste des RSS existants à une date, avec leur état d'utilisation et d'invalidation
 */
CREATE OR REPLACE FUNCTION V_Admin_RSSHeader_getValides(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT
  r.idheader,
  ReferValides.idheader IS NOT NULL AS Utilise,
  RSSInvalides.idheader IS NOT NULL AS Invalide
FROM RSSHeader r
-- Recherche de l'été actuel ou non du RSS
LEFT JOIN (
  SELECT MAX(R.idheader) AS idheader
  FROM RSSHeader R
  LEFT OUTER JOIN (
   -- récupération des RSS invalidés.
   SELECT H.idheader
   FROM RSSInvalidation H
   JOIN (-- Récupération de la dernière date d'un état d'invalidation par RSS
	 SELECT G.idheader, MAX(G.idInvalidation) AS idInvalidation
         FROM RSSInvalidation G
		 WHERE DateInvalidation < $1
         GROUP BY G.idheader) DernierEtatValidationRSS ON
      H.idInvalidation = DernierEtatValidationRSS.idInvalidation
    WHERE H.Invalidation = true) RSSInvalides ON
   R.idheader = RSSInvalides.idheader
  -- On ne garde que les RSS qui n'ont pas été dévalidés
  WHERE RSSInvalides.idheader IS NULL
  GROUP BY R.FINESS, R.FinPeriode) ReferValides ON
 r.idheader = ReferValides.idheader
LEFT JOIN (
  -- récupération des RSS invalidés.
   SELECT H.idheader
   FROM RSSInvalidation H
   JOIN (-- Récupération de la dernière date d'un état d'invalidation par RSS
         SELECT G.idheader, MAX(G.idInvalidation) AS idInvalidation
         FROM RSSInvalidation G
		 WHERE DateInvalidation < $1
         GROUP BY G.idheader) DernierEtatValidationRSS ON
      H.idInvalidation = DernierEtatValidationRSS.idInvalidation
    WHERE H.Invalidation = true) RSSInvalides ON
   r.idheader = RSSInvalides.idheader
WHERE r.DateAjout < $1;
$BODY$
  LANGUAGE 'sql' VOLATILE;

