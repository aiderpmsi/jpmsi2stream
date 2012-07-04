\nac
CREATE COLLECTION "Pmsi"&

CREATE DOCUMENT "PmsiDocIndice" IN COLLECTION "Pmsi"&
UPDATE INSERT <indice>{"1"}</indice> INTO fn:doc("PmsiDocIndice", "Pmsi")&

\commit

// ========== fonctions d'utilisation ==========

let $i:=fn:doc("PmsiDocIndice", "Pmsi")/indice
update
replace $l in $i
with <indice>{$l/text() + 1}</indice>
return $i

update
replace $l in fn:doc("PmsiDocIndice", "Pmsi")/indice
with <indice>{$l/text() + 1}</indice>&

// ========== fonctions de reporting ============

// Recherche des doublons de finess et heure (impossible de déterminer le dernier inséré)
for $i in distinct-values(fn:collection("Pmsi")/(RSF2009 | RSF2012)/content/@insertionTimeStamp)
for $l in distinct-values(fn:collection("Pmsi")/(RSF2009 | RSF2012)/content[@insertionTimeStamp = string($i)]/RsfHeader/@Finess)
let $items:=fn:collection("Pmsi")/(RSF2009 | RSF2012)/content[@insertionTimeStamp = $i]/RsfHeader[@Finess = $l]
order by $l, $i
return <entry finess="{string($l)}" date="{string($i)}" nb="{count($items)}">
{
  for $m in distinct-values($items/../../name())
  let $items2:=fn:collection("Pmsi")/*[name() = $m]/content[@insertionTimeStamp = $i]/RsfHeader[@Finess = $l]
  return <entry type="{$m}" nb="{count($items2)}"/>
}
</entry>&

// Recherche des doublons pour un finess particulier et une heure dinsertion particulière
let $items:=fn:collection("Pmsi")/(RSF2009 | RSF2012)[
  content/@insertionTimeStamp = "2012-07-03T16:24:43.969+02:00"
  and content/RsfHeader/@Finess = "300007119"]
return count($items)&

// Recherche du dernier rsf inséré par mois particulier et par finess
let $items:=fn:collection("Pmsi")/(RSF2009 | RSF2012)/content/RsfHeader
for $l in distinct-values($items/@Finess/string()),
    $y in distinct-values($items/year-from-date(xs:date(@DateFin))),
    $m in distinct-values($items/month-from-date(xs:date(@DateFin)))
order by $y, $m, $l 
return <entry finess="{$l}" monthfin="{$m}" yearfin="{$y}">
{
(for $item in fn:collection("Pmsi")/(RSF2009 | RSF2012)/content[RsfHeader/@Finess = $l and
                 RsfHeader/month-from-date(xs:date(@DateFin)) = $m and
                 RsfHeader/year-from-date(xs:date(@DateFin)) = $y]
order by $item/xs:dateTime(@insertionTimeStamp) descending
return <entry>{$item/@insertionTimeStamp}</entry>)[1]
}
</entry>&


