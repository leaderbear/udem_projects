--q1 Le nombre d'abonnés qui ont accumulé des frais de retard de plus de 5$. 

WITH R1 AS (SELECT id_membre,frais FROM Membre WHERE frais > 5)
SELECT COUNT(id_membre) AS nbr_membre FROM R1;


--q2 Âge moyen des membres ayant loué un livre du genre le plus populaire (le plus emprunté). 

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
SELECT AVG(age) AS age_moy,genre FROM R11 GROUP BY genre --âge moyen et le genre



--q3 obtenir une liste de noms et prenoms des membres et leur retard accumulé en nombre de jours total sur tous leurs emprunts, en ordre decroissant de jours de retard

WITH mem AS(SELECT id_membre, prenom, nom FROM Membre),
     emp AS (SELECT id_membre, date_du, date_retour FROM Emprunte
     	     WHERE COALESCE(date_retour, CURRENT_DATE)-date_du > 0),
     mememp AS (SELECT * FROM mem NATURAL JOIN emp)
SELECT prenom, nom,total
FROM mem NATURAL JOIN (
		SELECT id_membre,
       		       SUM(COALESCE(date_retour, CURRENT_DATE)-date_du) AS total
      		FROM mememp
       		GROUP BY id_membre
       		) AS foo
ORDER BY total DESC;



--q4 Le genre avec le plus grand nombre de commandes honorées et leur nombre de commandes honorées
with com as (select isbn from commande where date_emprunt is not null),
genres_com as (select genre, count(isbn) as nb_com from Genre join com using(isbn) group by genre),
max_genre as (select max(nb_com) as nb_com from genres_com)
select genre, nb_com from genres_com join max_genre using (nb_com);


