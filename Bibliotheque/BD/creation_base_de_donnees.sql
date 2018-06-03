-- Titre             : Script SQL (PostgreSQL) de création de la base de données du projet bibliothèque
-- Version           : 1.0
-- Date création     : 07 mars 2006
-- Date modification : 9 avril 2017
-- Auteur            : Philippe TANGUY
-- Description       : Ce script est une ébauche, à compléter, qui permet la création de la table
--                     "livre" pour la réalisation de la fonctionnalité "liste de tous les livres".
-- +----------------------------------------------------------------------------------------------+
-- | Suppression des tables                                                                       |
-- +----------------------------------------------------------------------------------------------+
drop table if exists "livre";
drop table if exists "usager";
drop table if exists "exemplaire";
drop table if exists "emprunt";
-- +----------------------------------------------------------------------------------------------+
-- | Création des tables                                                                          |
-- +----------------------------------------------------------------------------------------------+
-- Création de la table LIVRE 
create table livre
(
  id     serial primary key,
  isbn10 varchar(25) unique,
  isbn13 varchar(25) unique,
  titre  varchar(50) not null,
  auteur varchar(30)
);
-- Création de la table EXEMPLAIRE 
create table exemplaire
(
       id        serial primary key,
    livre_id        integer, 
        FOREIGN KEY (livre_id) REFERENCES livre (id)
);
-- Création de la table USAGER 
create table usager
(
        id        serial primary key,
        nom       varchar(25),
        prenom    varchar(50),
        statut    varchar(20) not null,
        email     varchar(60)
);

-- Création de la table EMPRUNT 
create table emprunt
(
        abonne_id                integer,
        exemplaire_id            integer,
        date_emprunt               timestamp,
        date_retour                timestamp,
        PRIMARY KEY (abonne_id, exemplaire_id,date_emprunt),
        FOREIGN KEY (abonne_id) REFERENCES usager(id),
        FOREIGN KEY (exemplaire_id) REFERENCES exemplaire (id)
);
-- +----------------------------------------------------------------------------------------------+
-- | Insertion de quelques données de pour les tests                                              |
-- +----------------------------------------------------------------------------------------------+
--Insertions dans la table LIVRE
insert into livre values(nextval('livre_id_seq'), '2-84177-042-7', NULL,                'JDBC et JAVA',                            'George REESE');    -- id = 1
insert into livre values(nextval('livre_id_seq'), NULL,            '978-2-7440-7222-2', 'Sociologie des organisations',            'Michel FOUDRIAT'); -- id = 2
insert into livre values(nextval('livre_id_seq'), '2-212-11600-4', '978-2-212-11600-7', 'Le data warehouse',                       'Ralph KIMBALL');   -- id = 3
insert into livre values(nextval('livre_id_seq'), '2-7117-4811-1', NULL,                'Entrepots de données',                    'Ralph KIMBALL');   -- id = 4
insert into livre values(nextval('livre_id_seq'), '2012250564',    '978-2012250567',    'Oui-Oui et le nouveau taxi',              'Enid BLYTON');     -- id = 5
insert into livre values(nextval('livre_id_seq'), '2203001011',    '978-2203001015',    'Tintin au Congo',                         'HERGÉ');           -- id = 6
insert into livre values(nextval('livre_id_seq'), '2012011373',    '978-2012011373',    'Le Club des Cinq et le trésor de l''île', 'Enid BLYTON');     -- id = 7

--Insertions dans la table USAGER
insert into usager values(nextval('usager_id_seq'), 'BAO',   'Caifeng',       'Etudiant' ,'caifeng.bao@imt-atlantique.net');    -- id = 1
insert into usager values(nextval('usager_id_seq'), 'ANIN',   'Kassi Arthus',   'Etudiant' ,'arthus.anin@imt-atlantique.net');  -- id = 2
insert into usager values(nextval('usager_id_seq'), 'SEGARA',   'MARIA',        'Professeur' ,'mt.segarra@imt-atlantique.net');   -- id = 3
insert into usager values(nextval('usager_id_seq'), 'BRISSON',   'Lorent',       'Professeur' ,'laurent.brisson@imt-atlantique.net');    -- id = 4
insert into usager values(nextval('usager_id_seq'), 'KOUADIO',   'Wilfried',       'Etudiant' ,'koffi-wilfried.kouadio@imt-atlantique.net');    -- id = 5

--Insertions dans la table EXEMPLAIRE
insert into exemplaire values(nextval('exemplaire_id_seq'), '1' );  -- id = 1
select * from exemplaire ;