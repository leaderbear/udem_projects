import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

public class MainAuto {

    // Version automatique de Main.Java.
    // Cette version est surtout utilisé pour le debug. On recommande l'autre version de notre code (Main.java)
    // qui demande de spécifier le chemin vers toutes les métriques. Cela dit, cette version peut
    // tout aussi bien marcher si la condition suivante est respectée:
    // Il est important que le dossier métrique sloit dans le même dossier contenant cette classe.
    public static void main(String[] args) throws IOException, ParseException {

        System.out.println("Début du programme Calculs des métriques en cours ...\n");
        String finalResult = "Sommaire des résultats des métriques:\n";
        Utilities utils = new Utilities();

        int q1 = 0;
        int q2 = 0;
        int q3 = 0;
        int q4 = 0;


        // Test time & # tests
        System.out.println("Calculer le temps de tests et le nombre de tests :\n");
        //System.out.println("Donnez le chemin complet du fichier  Test Results - All_in_jfreechart.xml, Exemple: \nC:\\Users\\leade\\Desktop\\IFT3913-TP2\\Metrics Data\\JUnit4\\Test Results - All_in_jfreechart.xml");
        //input = scanner.nextLine();
        try{
            System.out.println(System.getProperty("user.dir"));
            String tempsTestText = utils.findWordLineInFile(System.getProperty("user.dir") + "\\src\\Metrics Data\\JUnit4\\Test Results - All_in_jfreechart.xml", "<testrun duration=", 1, true);
            String duration = utils.getDegitsXamlTag(tempsTestText, "duration=\"", "\"");
            String total = utils.getDegitsXamlTag(tempsTestText, "value=\"", "\"");
            System.out.println("Temps de test : " + duration + "ms");
            System.out.println("Nombre total de tests : " + total);

            finalResult+= "\nTemps de test et nombre de tests avec le fichier Metrics Data\\JUnit4\\Test Results - All_in_jfreechart.xml." + "\nTemps de test :" + duration + "ms\nNombre total de tests : " + total +"\n";

            if (Integer.valueOf(duration) < 5000){
                q4++;
                finalResult+="La durée est inférieur à 5000ms, donc la note de la question 4 est maintenant :" + q4 + "/2\n";
            }

        }
        catch(Exception e){
            System.out.println( "chemin ou fichier invalide");
        }
        printSeparator();





        // PMNT
        System.out.println("Calculer PMNT :\n");
        //System.out.println("Donnez le chemin complet du fichier index.html généré par JUni4 Coverage Report, Exemple:\nC:\\Users\\leade\\Desktop\\IFT3913-TP2\\Metrics Data\\JUnit4\\Coverage Report\\index.html");
        //input = scanner.nextLine();
        try{
            String pmntText = utils.findWordLineInFile(System.getProperty("user.dir") +"\\src\\Metrics Data\\JUnit4\\Coverage Report\\index.html", "<td class=\"name\">all classes</td>", 12, true);
            //String pmntText = utils.findWordLineInFile(input, "<td class=\"name\">all classes</td>", 12, true);
            String pmnt = utils.getDegitsXamlTag(pmntText, "</span></td><tdclass=\"coverageStat\"><spanclass=\"percent\">", "");
            double pmntFixed = Double.parseDouble(pmnt.replace(',', '.'));
            System.out.println("PMNT : " + (100.0 - pmntFixed) + "%");
            if ((100.0 - pmntFixed) < 50.0){
                q3++;
                finalResult+="Le pmnt est inférieur à 50%, donc la note de la question 3 est maintenant :" + q3 +"/2\n";
            }
            finalResult+= "\nPMNT avec le fichier " + "Metrics Data\\JUnit4\\Coverage Report\\index.html" + "\nPMNT :" + (100 - pmntFixed) + "%\n";
        }catch (Exception e){
            System.out.println( "chemin ou fichier invalide");
        }
        printSeparator();





        // CBO & LCOM
        Map<String, Map<String, String>> ckMap;
        ArrayList<ArrayList<String>> ckOut;
        System.out.println("Calculer CBO & LCOM :\n");
        //ystem.out.println("Donnez le chemin complet du fichier outputclass.csv généré par l'outil ck, Exemple : \nC:\\Users\\leade\\Desktop\\IFT3913-TP2\\Metrics Data\\ck\\outputclass.csv");
       // input = scanner.nextLine();
        try{
            ckOut = utils.readCsv(System.getProperty("user.dir") + "\\src\\Metrics Data\\ck\\outputclass.csv");
            ckMap = utils.listToMap(ckOut);
            double cbo = 0.0;
            double lcom = 0.0;
            int lcomCount = 0;
            for (Map<String, String> file: ckMap.values()){
                cbo+= Integer.parseInt(file.get("cbo"));
                Double lcomI = Double.valueOf(file.get("lcom*"));
                if(!lcomI.isNaN()){
                    lcom = lcom +  Double.valueOf(file.get("lcom*"));
                    lcomCount++;
                }
            }
            double cboM = cbo/ckMap.size();
            double lcomM = lcom/lcomCount;

            System.out.println("cbo moyenne : " + cboM);
            System.out.println("lcom moyenne : " + lcomM);

            if ( 0.0 < cboM && cboM < 6.75){
                q2++;
                q4++;
                finalResult+="Le cbo est entre 0.0 exclu et 6.75, donc la note de la question 2 est maintenant :" + q4 + "/2\n";
                finalResult+="Le cbo est entre 0.0 exclu et 6.75, donc la note de la question 4 est maintenant :" + q4 + "/2\n";
            }

            if(lcomM < 0.6){
                q2++;
                finalResult+="Le lcom est inférieur à 0.6 exclu, donc la note de la question 2 est maintenant :" + q2+ "/2\n";
            }

            finalResult+= "\nCbo & Lcom avec  le fichier " + "Metrics Data\\ck\\outputclass.csv" + "\ncbo moyenne :" + cboM +  "\nlcom* moyenne :" + lcomM + "\n" ;
        }

        catch(Exception e){
            System.out.println( "chemin ou fichier invalide");
        }
        printSeparator();





        // Comments Density & #comments per method
        System.out.println("Calculer Comments Density et le nombre de commentaires par méthode :\n");
        //System.out.println("Re donnez le chemin complet du fichier outputclass.csv généré par l'outil ck, Exemple : \nC:\\Users\\leade\\Desktop\\IFT3913-TP2\\Metrics Data\\ck\\outputclass.csv");
        //input = scanner.nextLine();
        //System.out.println("Donnez le chemin complet du fichier cd.csv généré par notre outil, Exemple : \nC:\\Users\\leade\\Desktop\\IFT3913-TP2\\Metrics Data\\notreOutil\\cd.csv");
        //String inputExtra = scanner.nextLine();
        try{
            ckOut = utils.readCsv(System.getProperty("user.dir") + "\\src\\Metrics Data\\ck\\outputclass.csv");
            ckMap = utils.listToMap(ckOut);

            ArrayList<ArrayList<String>> cdOut = utils.readCsv( System.getProperty("user.dir") + "\\src\\Metrics Data\\notreOutil\\cd.csv");
            Map<String, Map<String, String>> cdMap = utils.listToMap(cdOut);

            double cd = 0.0;
            double commentsPerMethod = 0.0;
            int count = 0;
            for (Map<String, String> file: cdMap.values()) {
                cd += Double.parseDouble(file.get("cd"));

                String loc = file.get("location");
                Map<String, String> f = ckMap.get(loc);
                if (f != null){
                    count++;
                    int fileMethodsQty = Integer.parseInt(ckMap.get(loc).get("totalMethodsQty"));
                    double fileCloc = Double.parseDouble(file.get("cloc"));
                    commentsPerMethod+=  Double.valueOf(fileMethodsQty) / Double.valueOf(fileCloc);
                }
            }
            System.out.println("cd moyenne : " + cd/cdMap.size());
            System.out.println("#comments per method moyenne : " + commentsPerMethod / count );
            System.out.println("mean of mean(cd) & mean(#comments per method) : " + ((cd/cdMap.size()) + (commentsPerMethod/count)) / 2 );

            // Attribuer score
            if ( (0.15 < (cd/cdMap.size())) && ((cd/cdMap.size()) < 0.4)){
                q1++;
                finalResult+="La densité de commentaire est entre 0.15 et 0.4, donc la note de la question 1 est maintenant :" + q1 + "/2\n";
            }
            if ((0.15 < (commentsPerMethod / count)) && ((commentsPerMethod / count)< 0.4)){
                q1++;
                finalResult+="La nombre de commentaire par méthode est entre 0.15 et 0.4, donc la note de la question 1 est maintenant :" + q1 + "/2\n";
            }

            finalResult+= "\nDensité de commentaire et nombre de commentaire par méthode avec le fichier" + "Metrics Data\\notreOutil\\cd.csv" + "\nmoyenne densité de commentaire" + ((cd/cdMap.size()) + "Moyenne du nombre de commentaire par fonction: " + (commentsPerMethod/count)) + "\n" ;
        }catch (Exception e){
            System.out.println("Chemin ou fichier invalide.");
        }
        printSeparator();



        // Most recent creation date
        System.out.println("Trouver l'âge du plus jeune fichier java :\n");
        //System.out.println("Donnez le chemin complet du fichier creationDate.csv généré par notre outil, Exemple : \nC:\\Users\\leade\\Desktop\\IFT3913-TP2\\Metrics Data\\notreOutil\\creationDate.csv");
        //input = scanner.nextLine();

        //ArrayList<ArrayList<String>> cdateOut = utils.readCsv("C:\\Users\\leade\\Desktop\\IFT3913-TP2\\Metrics Data\\creationDate\\creationDate.csv");
        try{ArrayList<ArrayList<String>> cdateOut = utils.readCsv(System.getProperty("user.dir") +"\\src\\Metrics Data\\notreOutil\\creationDate.csv");
            Map<String, Map<String, String>> cdateMap = utils.listToMap(cdateOut);
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Map<String, String> file: cdateMap.values()) {
                if (date == null){
                    date = sdf.parse(file.get("creationDate"));
                }
                else{
                    if(sdf.parse(file.get("creationDate")).before(date)){
                        date = sdf.parse(file.get("creationDate"));
                    }
                }
            }

            Date today = new Date();
            Period diff = Period.between(convertToLocalDateViaInstant(today) , convertToLocalDateViaInstant(date));
            int mounthDiff = Math.abs(Integer.valueOf(diff.getYears()*12) + Integer.valueOf(diff.getMonths()));

            int note;
            if (mounthDiff >= 20) {
                note = 0;
            }else{
                note = 100 - (mounthDiff * 5);}

            System.out.println("Différence en mois : " + mounthDiff);
            System.out.println("La date la plus récente est : " + sdf.format(date));
            System.out.println("La note accordée est : " + note + " %.");
            finalResult+= "\nÂge du projet avec le fichier Metrics Data\\notreOutil\\creationDate.csv\");" + "\nDifférence en mois : " + mounthDiff + "\nLa date la plus récente est : " + sdf.format(date) + "\nLa note accordée est : " + note + " %.\n";

            if (note > 70){
                q3++;
                finalResult+="La note sur l'âge du project est supérieur à 70%, donc la note de la question 3 est maintenant :" + q3 + "/2\n";
            }
        }
        catch (Exception e){
            System.out.println(  "chemin ou fichier invalide");
        }
        finalResult+= "\nRéponses: (Rappel : la note doit être de 2/2 répondre Oui)\n";
        if (q1 == 2){
            finalResult += "Question 1: Oui.\n";
        }
        else{
            finalResult += "Question 1: Non.\n";
        }

        if (q2 == 2){
            finalResult += "Question 2: Oui.\n";
        }
        else {
            finalResult += "Question 2: Non.\n";
        }
        if (q3 == 2){
            finalResult += "Question 3: Oui.\n";
        }else {
            finalResult += "Question 3: Non.\n";
        }
        if (q4 == 2){
            finalResult += "Question 4: Oui.\n";
        }
        else{
            finalResult += "Question 4: Non.\n";
        }

        utils.writeTxtFile("result.txt", finalResult, true);
    }



    public static void printSeparator(){
        System.out.println("-----------------------------------------");
    }

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}