-- Daten externeId korrigieren, 2.7.09
update spielort set externeid = null;
--update ligateam set externeid = null;
update automatenaufsteller set externeid = null;

--update spielort set externeid=substr(externeid, 1, length(externeid)-2) where externeid like '%.%';
--update ligateam set externeid=substr(externeid, 1, length(externeid)-2) where externeid like '%.%';
--update automatenaufsteller set externeid=substr(externeid, 1, length(externeid)-2) where externeid like '%.%';
