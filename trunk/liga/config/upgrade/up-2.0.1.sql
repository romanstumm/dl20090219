-- Erweiterung, 08.02.2008, RST
-- fuer Abnahmeprotokoll Abs. 2.3, Nr 7
create table ligateamwunsch (
    wunschId SERIAL NOT NULL,
    team1 INTEGER NOT NULL,
    team2 INTEGER NOT NULL,
    wunschArt SMALLINT not null,
    CONSTRAINT PK_ligateamwunsch PRIMARY KEY (wunschId)
);

ALTER TABLE ligateamwunsch ADD CONSTRAINT wunsch_team1
    FOREIGN KEY (team1) REFERENCES ligateam(ligateamId);

ALTER TABLE ligateamwunsch ADD CONSTRAINT wunsch_team2
    FOREIGN KEY (team2) REFERENCES ligateam(ligateamId);

-- Migration
insert into ligateamwunsch(team1, team2, wunschArt)
    select wl.team1, wl.team2, 0 from whitelist wl;

ALTER TABLE whitelist DROP CONSTRAINT ligateam_whitelist;
ALTER TABLE whitelist DROP CONSTRAINT ligateam_whitelist2;
DROP TABLE whitelist;

-- fuer Abnahmeprotokoll Abs. 2.3, Nr 5
ALTER TABLE ligateamspiel ADD COLUMN fixiert BOOLEAN NOT NULL DEFAULT false;
