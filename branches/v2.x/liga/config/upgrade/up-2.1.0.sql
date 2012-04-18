-- Erweiterung, 21.03.2008, RSt
-- fuer Liga V2.1, Datenabgleich
ALTER TABLE automatenaufsteller ADD COLUMN externeId CHARACTER VARYING(5);
ALTER TABLE spielort ADD COLUMN externeId CHARACTER VARYING(5);
