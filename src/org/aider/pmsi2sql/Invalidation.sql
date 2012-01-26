/*
 * Table RSFInvalidation, permet de garder la trace des rsf invalid�s
 */
CREATE TABLE RSFInvalidation
  (idInvalidation bigserial NOT NULL,
   rsfheaderid bigint NOT NULL,
   DateInvalidation timestamp without time zone NOT NULL DEFAULT now(),
   Invalidation BOOLEAN NOT NULL,
   PRIMARY KEY (idInvalidation),
   FOREIGN KEY (rsfheaderid) REFERENCES rsfheader (rsfheaderid)
);

/*
 * Recherche pour chaque FINESS la liste des derniers rsf ins�r�s valide.
 */
CREATE OR REPLACE VIEW V_Admin_RSFHeader_Valides
AS
  SELECT MAX(R.rsfheaderid) AS rsfheaderid
  FROM RSFHeader R
  LEFT OUTER JOIN (
   -- r�cup�ration des RSF invalid�s.
   SELECT H.rsfheaderid
   FROM RSFInvalidation H
   JOIN (-- R�cup�ration de la derni�re date d'un �tat d'invalidation par rsf
	 SELECT G.rsfheaderid, MAX(G.idInvalidation) AS idInvalidation
         FROM RSFInvalidation G
         GROUP BY G.rsfheaderid) DernierEtatValidationRSF ON
      H.idInvalidation = DernierEtatValidationRSF.idInvalidation
    WHERE H.Invalidation = true) RSFInvalides ON
   R.rsfheaderid = RSFInvalides.rsfheaderid
  -- On ne garde que les RSF qui n'ont pas �t� d�valid�s
  WHERE RSFInvalides.rsfheaderid IS NULL
  GROUP BY R.FINESS, R.DateFin;

/*
 * Liste des rsf existants � une date, avec leur �tat d'utilisation et d'invalidation
 */
CREATE OR REPLACE FUNCTION V_Admin_RSFHeader_getValides(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT
  r.rsfheaderid,
  ReferValides.rsfheaderid IS NOT NULL AS Utilise,
  RSFInvalides.rsfheaderid IS NOT NULL AS Invalide
FROM RSFHeader r
-- Recherche de l'�t� actuel ou non du RSF
LEFT JOIN (
  SELECT MAX(R.rsfheaderid) AS rsfheaderid
  FROM RSFHeader R
  LEFT OUTER JOIN (
   -- r�cup�ration des RSF invalid�s.
   SELECT H.rsfheaderid
   FROM RSFInvalidation H
   JOIN (-- R�cup�ration de la derni�re date d'un �tat d'invalidation par rsf
	 SELECT G.rsfheaderid, MAX(G.idInvalidation) AS idInvalidation
         FROM RSFInvalidation G
		 WHERE DateInvalidation < $1
         GROUP BY G.rsfheaderid) DernierEtatValidationRSF ON
      H.idInvalidation = DernierEtatValidationRSF.idInvalidation
    WHERE H.Invalidation = true) RSFInvalides ON
   R.rsfheaderid = RSFInvalides.rsfheaderid
  -- On ne garde que les RSF qui n'ont pas �t� d�valid�s
  WHERE RSFInvalides.rsfheaderid IS NULL
  GROUP BY R.FINESS, R.DateFin) ReferValides ON
 r.rsfheaderid = ReferValides.rsfheaderid
LEFT JOIN (
  -- r�cup�ration des RSF invalid�s.
   SELECT H.rsfheaderid
   FROM RSFInvalidation H
   JOIN (-- R�cup�ration de la derni�re date d'un �tat d'invalidation par rsf
         SELECT G.rsfheaderid, MAX(G.idInvalidation) AS idInvalidation
         FROM RSFInvalidation G
		 WHERE DateInvalidation < $1
         GROUP BY G.rsfheaderid) DernierEtatValidationRSF ON
      H.idInvalidation = DernierEtatValidationRSF.idInvalidation
    WHERE H.Invalidation = true) RSFInvalides ON
   r.rsfheaderid = RSFInvalides.rsfheaderid
WHERE r.DateAjout < $1;
$BODY$
  LANGUAGE 'sql' VOLATILE;
  
 /*
 * Table RSSInvalidation, permet de garder la trace des RSS invalid�s
 */
CREATE TABLE RSSInvalidation
  (idInvalidation bigserial NOT NULL,
   idheader bigint NOT NULL,
   DateInvalidation timestamp without time zone NOT NULL DEFAULT now(),
   Invalidation BOOLEAN NOT NULL,
   PRIMARY KEY (idInvalidation),
   FOREIGN KEY (idheader) REFERENCES rssheader (idheader)
);

/*
 * Recherche pour chaque FINESS la liste des derniers rss ins�r�s valide.
 */
CREATE OR REPLACE VIEW V_Admin_RSSHeader_Valides
AS
  SELECT MAX(R.idheader) AS idheader
  FROM RSSHeader R
  LEFT OUTER JOIN (
   -- r�cup�ration des RSS invalid�s.
   SELECT H.idheader
   FROM RSSInvalidation H
   JOIN (-- R�cup�ration de la derni�re date d'un �tat d'invalidation par RSS
	 SELECT G.idheader, MAX(G.idInvalidation) AS idInvalidation
         FROM RSSInvalidation G
         GROUP BY G.idheader) DernierEtatValidationRSS ON
      H.idInvalidation = DernierEtatValidationRSS.idInvalidation
    WHERE H.Invalidation = true) RSSInvalides ON
   R.idheader = RSSInvalides.idheader
  -- On ne garde que les RSS qui n'ont pas �t� d�valid�s
  WHERE RSSInvalides.idheader IS NULL
  GROUP BY R.FINESS, R.FinPeriode;

/*
 * Liste des RSS existants � une date, avec leur �tat d'utilisation et d'invalidation
 */
CREATE OR REPLACE FUNCTION V_Admin_RSSHeader_getValides(TIMESTAMP)
RETURNS SETOF record AS
$BODY$
SELECT
  r.idheader,
  ReferValides.idheader IS NOT NULL AS Utilise,
  RSSInvalides.idheader IS NOT NULL AS Invalide
FROM RSSHeader r
-- Recherche de l'�t� actuel ou non du RSS
LEFT JOIN (
  SELECT MAX(R.idheader) AS idheader
  FROM RSSHeader R
  LEFT OUTER JOIN (
   -- r�cup�ration des RSS invalid�s.
   SELECT H.idheader
   FROM RSSInvalidation H
   JOIN (-- R�cup�ration de la derni�re date d'un �tat d'invalidation par RSS
	 SELECT G.idheader, MAX(G.idInvalidation) AS idInvalidation
         FROM RSSInvalidation G
		 WHERE DateInvalidation < $1
         GROUP BY G.idheader) DernierEtatValidationRSS ON
      H.idInvalidation = DernierEtatValidationRSS.idInvalidation
    WHERE H.Invalidation = true) RSSInvalides ON
   R.idheader = RSSInvalides.idheader
  -- On ne garde que les RSS qui n'ont pas �t� d�valid�s
  WHERE RSSInvalides.idheader IS NULL
  GROUP BY R.FINESS, R.FinPeriode) ReferValides ON
 r.idheader = ReferValides.idheader
LEFT JOIN (
  -- r�cup�ration des RSS invalid�s.
   SELECT H.idheader
   FROM RSSInvalidation H
   JOIN (-- R�cup�ration de la derni�re date d'un �tat d'invalidation par RSS
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

