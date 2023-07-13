package postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Db {
    private Connection connexion;
    private String server,database,user,password,error;


    public Db() {
        this.server = "localhost";
        this.database="biblio";
        this.user = "postgres";
        this.password = "postgres";
        connexion = null;
    }

    /**
     * initie la connection, en cas d'erreur on met l'erreur dans la variable error pour l'afficher dans le gui
     * @return
     */
    public Connection getConnection() {
        try {
            this.connexion = DriverManager.getConnection("jdbc:postgresql://"+server+"/"+database, user, password);
            System.out.println("la connexion avec le SGBD est bien établie....");
            return connexion;

        } catch (SQLException e) {
            error = e.getMessage();
        }
        return null;

    }

    /**
     * Exécute une requête sql et retourne le résultat sous forme de liste Row (liste de colonne)
     * @param sql la requête
     * @return liste de row
     */
    public ArrayList<Row> queryBD(String sql){
        try {
            Statement s = this.connexion.createStatement();
            ResultSet result = s.executeQuery(sql);
            ResultSetMetaData meta = result.getMetaData();

            ArrayList<Row> rows = new ArrayList<Row>();

            while(result.next()){
                ArrayList<Column> cols = new ArrayList<Column>();
                for(int j =1;j<=meta.getColumnCount();j++){
                    Column col = new Column(meta.getColumnName(j),result.getObject(j));
                    cols.add(col);
                }
                rows.add(new Row(cols));
            }
            return rows;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Row> question1() {
        return queryBD("SELECT COUNT ( public.membre.frais ) FROM public.membre where frais >= 5;");
    }

    public ArrayList<Row> question2(){
        return queryBD("WITH R1 AS (SELECT isbn,id_membre FROM Emprunte), --réduction largeur de Emprunte\n" +
                "R2 AS(SELECT isbn FROM R1),--réduction en largeur de R1\n" +
                "R3 AS(SELECT * FROM R2 NATURAL INNER JOIN Genre), --correspondance emprunt et isbn\n" +
                "R4 AS(SELECT genre,COUNT(isbn) AS nb_emp FROM R3 GROUP BY genre), --nombre d'emprunt pour chaque genre\n" +
                "R5 AS(SELECT MAX(nb_emp) AS nb_emp FROM R4), -- nombre d'enprunt le plus élevé\n" +
                "R6 AS(SELECT genre FROM R4 WHERE R4.nb_emp IN(SELECT * FROM R5)), --genres les plus populaires\n" +
                "R7 AS(SELECT Genre.genre,Genre.isbn FROM Genre WHERE Genre.genre IN(SELECT * FROM R6)), --isbn des livres parmis les genres les plus populaires\n" +
                "R8 AS(SELECT R1.id_membre,R7.genre FROM R1 NATURAL INNER JOIN R7 ), --id des membres ayant loué des livres parmi les genres les plus populaires\n" +
                "R9 AS(SELECT id_membre, date_naiss FROM membre), --réduction en largeur de Membre\n" +
                "R10 AS(SELECT R9.date_naiss,R8.genre FROM R9 NATURAL INNER JOIN R8), --date de naissance des membres\n" +
                "R11 AS(SELECT date_part('year', CURRENT_DATE) - date_part('year', date_naiss) AS age,genre FROM R10) --âge des membres et genre du livre\n" +
                "SELECT AVG(age) AS age_moy,genre FROM R11 GROUP BY genre --âge moyen et le genre");
    }

    public ArrayList<Row> question3() {
        return queryBD("WITH mem AS(SELECT id_membre, prenom, nom FROM Membre), "
					   + "emp AS (SELECT id_membre, date_du, date_retour FROM Emprunte "
					   + "WHERE COALESCE(date_retour, CURRENT_DATE)-date_du > 0), "
					   + "mememp AS (SELECT * FROM mem NATURAL JOIN emp) "
					   + "SELECT prenom, nom,total "
					   + "FROM mem NATURAL JOIN ( "
					   + "SELECT id_membre, "
					   + "SUM(COALESCE(date_retour, CURRENT_DATE)-date_du) AS total "
					   + "FROM mememp "
					   + "GROUP BY id_membre "
					   + ") AS foo ORDER BY total DESC;");
    }

    public ArrayList<Row> question4() {
        return queryBD("with com as (select isbn from commande where date_emprunt is not null),"
					   + "genres_com as (select genre, count(isbn) as nb_com from Genre join com using(isbn) group by genre),"
					   + "max_genre as (select max(nb_com) as nb_com from genres_com)"
					   + "select genre, nb_com from genres_com join max_genre using (nb_com);");
    }


    public Connection getConnexion() {
        return connexion;
    }

    public void setConnexion(Connection connexion) {
        this.connexion = connexion;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
       this.server = server;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public String getError(){return error;}

    public void setPassword(String password) {
        this.password = password;
    }

}
