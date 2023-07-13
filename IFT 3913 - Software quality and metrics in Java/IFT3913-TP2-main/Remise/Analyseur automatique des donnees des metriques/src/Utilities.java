import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utilities {

    /**
     *
     * @param filePath chemin du fichier csv
     * @return une liste de liste avec le contenu du fichier csv
     * @throws IOException
     */
    public ArrayList<ArrayList<String>> readCsv(String filePath) throws IOException {
        ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
        String line = null;
        BufferedReader stream = null;

        try {
            stream = new BufferedReader(new FileReader(filePath));
            while ((line = stream.readLine()) != null) {
                String[] splitted = line.split(",");
                result.add(new ArrayList<>(Arrays.asList(splitted)));
            }
        } finally {
            if (stream != null)
                stream.close();
        }
        return result;
    }

    /**
     *
     * @param path Chemin du fichier
     * @param word le mot recherché
     * @param nextLines combien de lignes en plus de la ligne contenant le mot retourner
     * @param removeSpace enlever tous les espaces du string
     * @return
     */
    public String findWordLineInFile(String path, String word, int nextLines, boolean removeSpace){
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);
            int lineNum = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                String finalResult = "";
                if(line.length() > 0 && line.contains(word)) {

                    //scanner.close();
                    if (nextLines == 0){
                        finalResult+= line;}
                    else{
                        finalResult+= line;
                        for (int i = 0; i < nextLines; i++){
                            if (scanner.hasNextLine()){
                                finalResult+= scanner.nextLine();}
                        }
                    }
                    if (removeSpace){
                        finalResult = finalResult.replaceAll("\\s+","");
                    }

                    return finalResult;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    // Source :  https://devqa.io/extract-numbers-string-java-regular-expressions// /
    /**
     *
     * @param line Le texte dans lequel on cherche
     * @param tagPrefix chercher ce préfix
     * @param tagPostfix chercher ce suffix
     * @return envoyer le chiffre entre le préfix et le suffit
     */
    public String getDegitsXamlTag(String line, String tagPrefix, String tagPostfix) {
        String pat = tagPrefix + "([0-9,.]+)" + tagPostfix;
        Pattern pattern = Pattern.compile(pat);
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            //System.out.println(matcher.group(1));
            return matcher.group(1);
        }
        return "";
    }

    /**
     *
     * @param list La première liste est le header (futurs clé du dictionnaire), les autres liste seront les valeurs
     * @return Map le header avec toutes les autres liste de notre argument et retourne le dictionnaire
     */
    public Map<String, Map<String, String>> listToMap( ArrayList<ArrayList<String>> list){
        Map<String, Map<String, String>> result =  new HashMap<String, Map<String, String>>();
        ArrayList<String> header = list.get(0);

        for(int i = 1; i < list.size(); i++){

            ArrayList<String> line = list.get(i);

            Map<String, String> tempDic = new HashMap<String, String>();

            for(int j = 0; j < line.size(); j++){
                tempDic.put(header.get(j), line.get(j));
            }
            result.put(line.get(0), tempDic);
        }
        return result;
    }

    /**
     * Read every JavaFile.java in this.list. Write its information in a line that will be written later on the csv.
     * @param filePath is where we will write the file
     * @param writeConsole Do we write the data that has been written in the file in console too?
     * @return the written file itself.
     * @throws IOException
     */
    public File writeTxtFile(String filePath, String content, boolean writeConsole) throws IOException {
        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        if (writeConsole){
            System.out.print(content);
        }
        System.out.println("File written in : " + file.getAbsolutePath());
        fileWriter.close();
        return file;
    }

}
