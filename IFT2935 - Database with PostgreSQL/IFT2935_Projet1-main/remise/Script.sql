-- Table: public.biblio

-- DROP TABLE public.biblio;

-----------------------------------CREATION DE TABLES-----------------------------------
BEGIN TRANSACTION;

CREATE TABLE Livre (
    isbn INTEGER NOT NULL UNIQUE PRIMARY KEY,
    titre TEXT NOT NULL,
    editeur TEXT NOT NULL
);

CREATE TABLE Auteur (
    isbn INTEGER NOT NULL,
    auteur TEXT NOT NULL,
	CONSTRAINT FK_isbn_Auteur          
        FOREIGN KEY (isbn)             
        REFERENCES Livre(isbn),
	PRIMARY KEY (isbn, auteur)
);

CREATE TABLE Genre (
    isbn INTEGER NOT NULL,
    genre TEXT NOT NULL,
	CONSTRAINT FK_isbn_Genre          
        FOREIGN KEY (isbn)             
        REFERENCES Livre(isbn),
	PRIMARY KEY (isbn, genre)
);

CREATE TABLE statuts(
	statut text primary key
);

CREATE TABLE Exemplaire (
       isbn INTEGER NOT NULL,
       no_exemplaire INTEGER NOT NULL,
       statut TEXT NOT NULL,
	CONSTRAINT FK_isbn_Exemplaire          
        FOREIGN KEY (isbn)             
        REFERENCES Livre(isbn),
	CONSTRAINT FK_statut
	FOREIGN KEY (statut) REFERENCES statuts(statut),
	PRIMARY KEY (isbn, no_exemplaire)

);
INSERT INTO Statuts VALUES ('Indisponible'), ('Commande'), ('Consulation'), ('Disponible'), ('Manquant'), ('Perdu'), ('Prêt'), ('Prêté'), ('Réservé');

CREATE TABLE Bloc_postal (
	cp TEXT NOT NULL PRIMARY KEY,
	rue TEXT NOT NULL,
	ville TEXT NOT NULL

);

CREATE TABLE Membre (
       id_membre SERIAL NOT NULL PRIMARY KEY,
       prenom TEXT NOT NULL,
       nom TEXT NOT NULL,
       courriel TEXT NOT NULL,
       frais FLOAT DEFAULT 0,
       date_exp DATE DEFAULT CURRENT_DATE + INTERVAL '2 year',
       date_naiss DATE NOT NULL,
       apt INTEGER,
       no_civ INTEGER NOT NULL,
       cp TEXT NOT NULL,
       CONSTRAINT Fk_cp
       FOREIGN KEY (cp)
       REFERENCES Bloc_postal(cp),
       UNIQUE (courriel)
);



CREATE TABLE Emprunte (
    id_membre SERIAL NOT NULL,
    isbn INTEGER NOT NULL,
    no_exemplaire INTEGER NOT NULL,
    date_emprunt DATE NOT NULL default CURRENT_DATE,
    date_du DATE NOT NULL CHECK (date_du > date_emprunt + 13),
    date_retour DATE CHECK (date_retour > date_emprunt),
	CONSTRAINT FK_id_emprunte 
		FOREIGN KEY (id_membre) 
		REFERENCES Membre(id_membre),
	CONSTRAINT Fk_exemlaire
		   FOREIGN KEY (isbn, no_exemplaire) 
		   REFERENCES Exemplaire (isbn, no_exemplaire),
	PRIMARY KEY (isbn, no_exemplaire, date_emprunt)
);


CREATE TABLE Commande (
	id_membre SERIAL NOT NULL,
	isbn INTEGER NOT NULL,
	no_exemplaire INTEGER NOT NULL,
	date_commande TIMESTAMP NOT NULL default CURRENT_DATE,
	date_dispo DATE,
	date_emprunt DATE,
	annul BOOLEAN DEFAULT false, 
	CONSTRAINT FK_id_Commande 
		FOREIGN KEY (id_membre) 
		REFERENCES Membre(id_membre),
	CONSTRAINT FK_isbn_exemplaire_Commande 
		FOREIGN KEY (isbn, no_exemplaire) 
		REFERENCES Exemplaire(isbn, no_exemplaire),
	PRIMARY KEY (id_membre, isbn, no_exemplaire, date_commande)
); 

CREATE OR REPLACE FUNCTION max_commande()
  RETURNS TRIGGER AS
$func$
BEGIN
   IF EXISTS (
         SELECT COUNT(id_membre) from Commande where date_emprunt IS NULL and not annul having count(id_membre)>= 4 ) THEN
     RAISE EXCEPTION 'Un membre peut commander trois livre au maximum';
   END IF;
   RETURN NEW;
END
$func$  LANGUAGE plpgsql;

CREATE TRIGGER commande_limit BEFORE INSERT 
       ON commande
       FOR EACH ROW
       EXECUTE PROCEDURE max_commande();


CREATE FUNCTION default_date_du()
RETURNS trigger AS '
BEGIN
	IF NEW.date_du IS NULL THEN
	    NEW.date_du := NEW.date_emprunt + 14;
	END IF;
	RETURN NEW;
END' LANGUAGE 'plpgsql';

CREATE TRIGGER df_date_du BEFORE INSERT 
       ON Emprunte
       FOR EACH ROW
       EXECUTE PROCEDURE default_date_du();

CREATE VIEW membre_info as
       select id_membre, prenom, nom, date_naiss, no_civ, rue, ville, cp, date_exp, frais
       from membre join bloc_postal using (cp);

COMMIT;




-------------------------------------INSERTIONS-------------------------------------

begin transaction;
INSERT INTO Livre VALUES ('00001','Un café avec marie','Boréal');
INSERT INTO Livre VALUES ('00002','Les Enfants sont rois','Gallimard');
INSERT INTO Livre VALUES ('00003','perdre 50 kilo en 3 jours','Vivre');
INSERT INTO Livre VALUES ('00004','le portrait','capture');
INSERT INTO Livre VALUES ('00005','Origines évolutive' ,'Boréal');
INSERT INTO Livre VALUES ('00006','Connaitre','Savoir');
INSERT INTO Livre VALUES ('00007','Quebec libre','liberte');
INSERT INTO Livre VALUES ('00008','Moyen-âge','Historia');
INSERT INTO Livre VALUES ('00009','one piece','zorro');
INSERT INTO Livre VALUES ('00010','La disgrâce','lu');
INSERT INTO Livre VALUES ('00011', 'In Clothes Called Fat', 'Vertical');
INSERT INTO Livre VALUES ('00012', 'A Girl on the Shore', 'Vertical Comics');
INSERT INTO Livre VALUES ('00013', 'Realities', 'Heibonsha');
INSERT INTO Livre VALUES ('00014', 'Helter Skelter', 'Vertical');
INSERT INTO Livre VALUES ('00015', 'Pink', 'Vertical');
INSERT INTO Livre VALUES ('00016', 'Cartoons', 'kadokawashoten');


INSERT INTO Auteur VALUES ('00001','Serge Bouchard');
INSERT INTO Auteur VALUES ('00002','DELPHINE DE VIGAN');
INSERT INTO Auteur VALUES ('00003','Patrick Legros');
INSERT INTO Auteur VALUES ('00004','picasso del peint');
INSERT INTO Auteur VALUES ('00005','Marc Levieux');
INSERT INTO Auteur VALUES ('00006','Pierre Mailloux');
INSERT INTO Auteur VALUES ('00007','Charles de Gaulle');
INSERT INTO Auteur VALUES ('00008','Chritophe coloumbo');
INSERT INTO Auteur VALUES ('00009','Eiichirō Oda');
INSERT INTO Auteur VALUES ('00010','Nicol Avril');
INSERT INTO Auteur VALUES ('00011', 'Moyoko Anno');
INSERT INTO Auteur VALUES ('00012', 'Inio Asano');
INSERT INTO Auteur VALUES ('00013', 'Kyoko Okazaki'); 
INSERT INTO Auteur VALUES ('00014', 'Kyoko Okazaki');
INSERT INTO Auteur VALUES ('00015', 'Kyoko Okazaki');
INSERT INTO Auteur VALUES ('00016', 'Kyoko Okazaki');

INSERT INTO Genre VALUES ('00001','Littérature Québécoise');
INSERT INTO Genre VALUES ('00002','Littérature Française');
INSERT INTO Genre VALUES ('00003','Santé');
INSERT INTO Genre VALUES ('00004','Arts');
INSERT INTO Genre VALUES ('00005','Sciences');
INSERT INTO Genre VALUES ('00006','Savoir');
INSERT INTO Genre VALUES ('00007','Société');
INSERT INTO Genre VALUES ('00008','Histoire');
INSERT INTO Genre VALUES ('00009','Manga');
INSERT INTO Genre VALUES ('00010','Drame');
INSERT INTO Genre VALUES ('00011', 'Manga');
INSERT INTO Genre VALUES ('00012', 'Manga');
INSERT INTO Genre VALUES ('00013', 'Manga');
INSERT INTO Genre VALUES ('00014', 'Manga');
INSERT INTO Genre VALUES ('00015', 'Manga');
INSERT INTO Genre VALUES ('00016', 'Manga');


INSERT INTO Exemplaire VALUES ('00001', '10001','Disponible');
INSERT INTO Exemplaire VALUES ('00002', '10002','Disponible');
INSERT INTO Exemplaire VALUES ('00003', '10003','Disponible');
INSERT INTO Exemplaire VALUES ('00004', '10004','Disponible');
INSERT INTO Exemplaire VALUES ('00005', '10005','Disponible');
INSERT INTO Exemplaire VALUES ('00006', '10006','Disponible');
INSERT INTO Exemplaire VALUES ('00007', '10007','Disponible');
INSERT INTO Exemplaire VALUES ('00008', '10008','Disponible');
INSERT INTO Exemplaire VALUES ('00009', '10009','Disponible');
INSERT INTO Exemplaire VALUES ('00010', '10010','Disponible');
INSERT INTO Exemplaire VALUES ('00011', '10011', 'Disponible');
INSERT INTO Exemplaire VALUES ('00012', '10012', 'Disponible');
INSERT INTO Exemplaire VALUES ('00013', '10013', 'Disponible');
INSERT INTO Exemplaire VALUES ('00014', '10014', 'Disponible');
INSERT INTO Exemplaire VALUES ('00015', '10015', 'Disponible');
INSERT INTO Exemplaire VALUES ('00016', '10016', 'Disponible');


INSERT INTO Bloc_postal VALUES ('H17U4F','1er avenue','Montreal');
INSERT INTO Bloc_postal VALUES ('R17U2J','belanger','Montreal');
INSERT INTO Bloc_postal VALUES ('JR7D4F','des sources','Montreal');
INSERT INTO Bloc_postal VALUES ('G4H68D','12er avenue','Granby');
INSERT INTO Bloc_postal VALUES ('S94H37','deragon','Longeuil');
INSERT INTO Bloc_postal VALUES ('J37DD6','pierre-pagnan','Montreal');
INSERT INTO Bloc_postal VALUES ('2E6H4T','10er avenue','Richelieu');
INSERT INTO Bloc_postal VALUES ('J8F6F6','jean-talon','Montreal');
INSERT INTO Bloc_postal VALUES ('K5H56S','peel','Montreal');
INSERT INTO Bloc_postal VALUES ('H3TS6D','52er avenue','Quebec');
INSERT INTO Bloc_postal VALUES ('H17U4X','1er avenue','Quebec');

INSERT INTO Membre (id_membre, prenom, nom, date_naiss, courriel, no_civ, cp) VALUES ('010001','Eva','Smith', '2000-02-01','EvaSmith@hotmail.com','227', 'H17U4F');
INSERT INTO Membre (id_membre, prenom, nom, date_naiss, courriel, no_civ, cp) VALUES ('010002','jean','jack', '2001-04-06','jeanjack@hotmail.com','375', 'R17U2J');
INSERT INTO Membre (id_membre, prenom, nom, date_naiss, courriel, no_civ, cp) VALUES ('010003','stephane','loa', '1990-11-12','stephaneloa@hotmail.com','1647', 'JR7D4F');
INSERT INTO Membre (id_membre, prenom, nom, date_naiss, courriel, no_civ, cp) VALUES ('010004','momo','Sif', '2000-09-03','momoSif@hotmail.com','8573', 'G4H68D');
INSERT INTO Membre (id_membre, prenom, nom, date_naiss, courriel, no_civ, cp) VALUES ('010005','kader','Rohik', '1999-10-08','kaderRohik@hotmail.com','854', 'S94H37');
INSERT INTO Membre (id_membre, prenom, nom, date_naiss, courriel, no_civ, cp) VALUES ('010006','Samet','Lourd', '1993-04-12','SametLourd@hotmail.com','954', 'J37DD6');
INSERT INTO Membre (id_membre, prenom, nom, date_naiss, courriel, no_civ, cp) VALUES ('010007','Jean','Bouchard', '2002-09-24','JeanBouchard@hotmail.com','257', '2E6H4T');
INSERT INTO Membre (id_membre, prenom, nom, date_naiss, courriel, apt, no_civ, cp) VALUES ('010008','Bonnie','Clide', '1996-02-27','BonnieClide@hotmail.com','11','7304', 'J8F6F6');
INSERT INTO Membre (id_membre, prenom, nom, date_naiss, courriel, apt, no_civ, cp) VALUES ('010009','Jason','Johnson', '1997-04-16','JasonJohnson@hotmail.com','1','9836', 'K5H56S');
INSERT INTO Membre (id_membre, prenom, nom, date_naiss, courriel, apt, no_civ, cp) VALUES ('010010','George','Reed', '1987-05-21','GeorgeReed@hotmail.com','202','4933', 'H3TS6D');
INSERT INTO Membre (id_membre, prenom, nom, date_naiss, courriel, apt, no_civ, cp) VALUES ('10011','Franklin','Iacovo', '1994-02-01','FranklinIa@hotmail.com','49','2272', 'H17U4X');
INSERT INTO Membre (id_membre, prenom, nom, frais, date_naiss, courriel, no_civ, cp) VALUES ('10111','emile','elime','6', '1994-02-01', 'Emilee@hotmail.com','2272', 'H17U4X');
INSERT INTO Membre (id_membre, prenom, nom, frais, date_naiss, courriel, no_civ, cp) VALUES ('10012','ben','affleck','6', '1984-04-11','benaf@hotmail.com' ,'2122', 'J8F6F6');
INSERT INTO Membre (id_membre, prenom, nom, frais, date_naiss, courriel, no_civ, cp) VALUES ('10013','frank','sinatra','4', '1990-06-06', 'franksin@hotmail.com','4872', 'S94H37');
INSERT INTO Membre (id_membre, prenom, nom, frais, date_naiss, courriel, no_civ, cp) VALUES ('10014','lola','rosa','7', '1997-08-24', 'lolarosa@hotmail.com','3768', 'G4H68D');
INSERT INTO Membre (id_membre, prenom, nom, frais, date_naiss, courriel, no_civ, cp) VALUES ('10015','naruto','uzumaki','8', '1994-09-30','narutouzu@hotmail.com','5846', 'H17U4X');
INSERT INTO Membre (id_membre, prenom, nom, frais, date_naiss, courriel, no_civ, cp) VALUES ('10016','ichigo','kurosaki', '6', '1995-12-21','ichigokuro@hotmail.com','3875', 'R17U2J');


INSERT INTO Emprunte VALUES ('010001','00006','10006','2021-02-10', '2021-02-24', '2021-02-14');
INSERT INTO Emprunte VALUES ('010002','00004','10004','2021-03-13', '2021-03-27', '2021-03-18');
INSERT INTO Emprunte VALUES ('010002','00001','10001','2021-03-18', '2021-04-03', '2021-04-01');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10003', 1, 10001, '2020-12-30', '2020-12-31');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10009', 2, 10002, '2020-05-09', '2020-06-18');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10005', 10, 10010, '2020-01-16', '2020-01-17');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10008', 3, 10003, '2020-03-04', '2020-04-25');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10007', 1, 10001, '2021-01-21', '2021-02-07');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10009', 8, 10008, '2020-12-10', '2021-01-27');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10004', 8, 10008, '2020-08-09', '2020-09-23');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10004', 1, 10001, '2020-07-13', '2020-07-15');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10008', 6, 10006, '2021-03-02', '2021-03-03');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10009', 5, 10005, '2020-01-16', '2020-02-07');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10008', 9, 10009, '2021-03-23', '2021-03-28');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10010', 3, 10003, '2020-02-24', '2020-02-28');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10003', 2, 10002, '2020-08-14', '2020-08-17');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10001', 2, 10002, '2020-02-09', '2020-02-11');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10006', 1, 10001, '2020-01-06', '2020-01-08');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10006', 3, 10003, '2020-01-09', '2020-02-16');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10008', 5, 10005, '2020-04-23', '2020-04-28');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10002', 5, 10005, '2021-02-24', '2021-02-28');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10001', 1, 10001, '2020-05-18', '2020-05-28');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10005', 9, 10009, '2020-06-05', '2020-06-19');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10004', 5, 10005, '2020-10-06', '2020-10-10');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10001', 7, 10007, '2020-09-20', '2020-09-29');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10002', 2, 10002, '2021-03-10', '2021-03-24');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10002', 6, 10006, '2020-02-04', '2020-03-12');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10006', 10, 10010, '2020-11-14', '2020-12-26');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10006', 7, 10007, '2020-01-27', '2020-01-30');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10006', 2, 10002, '2021-02-01', '2021-03-11');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10007', 10, 10010, '2021-02-21', '2021-02-24');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10001', 5, 10005, '2020-05-27', '2020-06-13');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10001', 4, 10004, '2020-11-09', '2020-12-09');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10010', 2, 10002, '2020-10-24', '2020-11-14');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10010', 4, 10004, '2021-03-01', '2021-04-19');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10009', 3, 10003, '2020-08-10', '2020-08-15');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10003', 10, 10010, '2020-06-24', '2020-07-02');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10007', 4, 10004, '2020-08-04', '2020-09-08');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10004', 9, 10009, '2021-03-15', '2021-04-07');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10010', 8, 10008, '2020-02-27', '2020-03-07');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10001', 10, 10010, '2020-07-03', '2020-07-13');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10003', 8, 10008, '2020-06-04', '2020-07-11');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10009', 10, 10010, '2020-08-15', '2020-09-19');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10008', 1, 10001, '2020-01-21', '2020-01-29');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10005', 3, 10003, '2020-12-13', '2021-01-03');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10010', 10, 10010, '2020-10-13', '2020-10-14');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10006', 4, 10004, '2020-10-29', '2020-11-28');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10003', 7, 10007, '2020-12-13', '2020-12-15');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10003', 6, 10006, '2021-04-03', '2021-05-03');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10010', 6, 10006, '2020-02-11', '2020-02-13');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10005', 6, 10006, '2020-03-26', '2020-04-01');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10008', 7, 10007, '2020-10-26', '2020-11-05');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10002', 10, 10010, '2020-12-20', '2020-12-22');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10010', 5, 10005, '2020-03-20', '2020-04-14');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10002', 3, 10003, '2020-08-21', '2020-09-21');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10005', 5, 10005, '2020-12-05', '2021-01-20');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10005', 8, 10008, '2020-05-29', '2020-06-20');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10004', 4, 10004, '2020-07-28', '2020-08-20');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10005', 7, 10007, '2021-02-24', '2021-03-15');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10007', 8, 10008, '2021-03-15', '2021-03-25');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10006', 8, 10008, '2020-02-16', '2020-02-18');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10011', 2, 10002, '2021-03-07', '2021-03-12');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10011', 4, 10004, '2020-02-15', '2020-03-17');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10011', 2, 10002, '2021-02-21', '2021-03-12');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10011', 3, 10003, '2020-03-17', '2020-03-20');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10011', 2, 10002, '2021-02-24', '2021-03-26');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10011', 1, 10001, '2021-03-01', '2021-03-03');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10011', 7, 10007, '2020-11-19', '2020-11-21');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10011', 8, 10008, '2021-03-31', '2021-04-04');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10011', 2, 10002, '2020-12-28', '2021-01-03');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt, date_retour) values ('10011', 3, 10003, '2020-02-14', '2020-02-15');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt) values ('10002', 5, 10005, '2020-09-04');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt) values ('10008', 9, 10009, '2020-11-08');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt) values ('10001', 2, 10002, '2020-12-03');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt) values ('10001', 2, 10002, '2020-07-23');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt) values ('10005', 3, 10003, '2020-09-22');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt) values ('10004', 5, 10005, '2020-03-01');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt) values ('10003', 1, 10001, '2020-02-24');
insert into Emprunte (id_membre, isbn, no_exemplaire, date_emprunt) values ('10010', 4, 10004, '2020-10-16');




insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt, annul) values ('10003', 1, 10001, '2020-11-25', '2020-11-30', '2020-12-05', DEFAULT);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt, annul) values ('10009', 2, 10002, '2020-04-09', '2020-05-18', '2020-05-20', DEFAULT);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt, annul) values ('10005', 10, 10010, '2020-01-01', '2020-01-07','2020-01-07', DEFAULT);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt, annul) values ('10008', 3, 10003, '2020-02-04', '2020-03-25', '2020-03-25', DEFAULT);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt, annul) values ('10007', 1, 10001, '2021-01-11', '2021-01-17', '2021-01-17', DEFAULT);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt, annul) values ('10009', 8, 10008, '2020-11-10', '2020-12-27', '2020-12-27', DEFAULT);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt, annul) values ('10004', 8, 10008, '2020-07-09', '2020-08-23', '2020-08-23', DEFAULT);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt, annul) values ('10004', 1, 10001, '2020-06-13', '2020-06-15', '2020-06-15', DEFAULT);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, annul) values ('10008', 6, 10006, '2021-02-02', '2021-02-03',  True);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, annul) values ('10009', 5, 10005, '2020-01-03', '2020-01-07',  True);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, annul) values ('10008', 9, 10009, '2021-02-23', '2021-02-28',  True);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, annul) values ('10111', '00009', '10009','2021-01-21', '2021-02-12', True);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, annul) values ('10012', '00010', '10010','2020-02-17', '2020-02-20', True);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, annul) values ('10013', '00011', '10011','2021-01-24', '2021-02-26', True);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, annul) values ('10014', '00012', '10012','2021-02-01', '2021-02-03', True);
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt) values ('10015', '00013', '10013','2020-10-19', '2020-10-21', '2020-10-21');
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt) values ('10016', '00014', '10014','2021-02-21', '2021-03-01', '2021-03-04');
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt) values ('10001', '00015', '10015','2020-11-28', '2020-12-03', '2020-12-03');
insert into Commande (id_membre, isbn, no_exemplaire, date_commande, date_dispo, date_emprunt) values ('10002', '00016', '10016','2020-01-14', '2020-01-15', '2020-01-15');
INSERT INTO Commande VALUES ('010002','00001','10001','2021-03-11','2021-03-17','2021-03-18', 'False');
INSERT INTO Commande VALUES ('010008','00003','10003','2021-02-14','2021-02-19','2021-02-20', 'False');


SELECT * FROM Livre;
SELECT * FROM Auteur;
SELECT * FROM Genre;
SELECT * FROM Exemplaire;
SELECT * FROM Membre;
SELECT * FROM Bloc_postal;
SELECT * FROM Emprunte;
SELECT * FROM Commande;

commit;



-------------------------------------QUESTIONS-------------------------------------

 
--q1
--Le nombre d'abonnés qui ont accumulé des frais de retard de plus de 5$. 

WITH R1 AS (SELECT id_membre,frais FROM Membre WHERE frais > 5)
SELECT COUNT(id_membre) AS nbr_membre FROM R1;


--q2
--Âge moyen des membres ayant loué un livre du genre le plus populaire (le plus emprunté). 

WITH R1 AS (SELECT isbn,id_membre FROM Emprunte), --réduction largeur de Emprunte
R2 AS(SELECT isbn FROM R1),	--réduction en largeur de R1
R3 AS(SELECT * FROM R2 NATURAL INNER JOIN Genre), --correspondance emprunt et isbn
R4 AS(SELECT genre,COUNT(isbn) AS nb_emp FROM R3 GROUP BY genre), --nombre d'emprunt pour chaque genre
R5 AS(SELECT MAX(nb_emp) AS nb_emp FROM R4),	-- nombre d'enprunt le plus élevé
R6 AS(SELECT genre FROM R4 WHERE R4.nb_emp IN(SELECT * FROM R5)), --genres les plus populaires
R7 AS(SELECT Genre.genre,Genre.isbn FROM Genre WHERE Genre.genre IN(SELECT * FROM R6)), --isbn des livres parmis les genres les plus populaires
R8 AS(SELECT R1.id_membre,R7.genre FROM R1 NATURAL INNER JOIN R7 ), --id des membres ayant loué des livres parmi les genres les plus populaires
R9 AS(SELECT id_membre, date_naiss FROM membre),	--réduction en largeur de Membre
R10 AS(SELECT R9.date_naiss,R8.genre FROM R9 NATURAL INNER JOIN R8), --date de naissance des membres
R11 AS(SELECT date_part('year', CURRENT_DATE) - date_part('year', date_naiss) AS age,genre FROM R10) --âge des membres et genre du livre
SELECT AVG(age) AS age_moy,genre FROM R11 GROUP BY genre; --âge moyen et le genre



--q3
--Liste de noms et prenoms des membres et leur retard accumulé en nombre de jours total sur tous leurs emprunts, en ordre decroissant de jours de retard

WITH mem AS(SELECT id_membre, prenom, nom FROM Membre),
     emp AS (SELECT id_membre, date_du, date_retour FROM Emprunte
     	     WHERE COALESCE(date_retour, CURRENT_DATE)-date_du > 0),
     mememp AS (SELECT * FROM mem NATURAL JOIN emp)
SELECT prenom, nom, retard_total
FROM mem NATURAL JOIN (
     	 	 SELECT id_membre,
		 	SUM(COALESCE(date_retour, CURRENT_DATE)-date_du) AS retard_total
      		 FROM mememp
       		 GROUP BY id_membre
       		 ) AS foo
ORDER BY retard_total DESC;



--q4
--Le(s) genre(s) avec le plus grand nombre de commandes honorées ainsi que le nombre de ces commandes
WITH com AS (SELECT isbn FROM commande WHERE date_emprunt IS NOT NULL),
genres_com AS (SELECT genre, COUNT(isbn) AS nb_com FROM Genre JOIN com USING(isbn) GROUP BY genre),
max_genre AS (SELECT MAX(nb_com) AS nb_com FROM genres_com)
SELECT genre, nb_com FROM genres_com JOIN max_genre USING (nb_com);


