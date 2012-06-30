\nac
CREATE COLLECTION "Pmsi"&

CREATE DOCUMENT "PmsiList" IN COLLECTION "Pmsi"&
UPDATE INSERT <indice>{"1"}</indice> INTO fn:doc("PmsiDocIndice", "Pmsi")&

\commit

let $i:=fn:doc("PmsiDocIndice", "Pmsi")/indice
update
replace $l in $i
with <indice>{$l/text() + 1}</indice>
return $i

update
replace $l in fn:doc("PmsiDocIndice", "Pmsi")/indice
with <indice>{$l/text() + 1}</indice>&