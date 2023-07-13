import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utilities {


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


    public boolean isPerfectMatchString(String line, String word){
        String[] splitted = line.split("\\W");
        for (String s : splitted){
            if (word.equals(s)){
                return true;
            }
        }
        return false;
    }

    public boolean isComment(String S)
    {
        char line[] = S.toCharArray();
        if (line[0] == '/' && line[1] == '/' )
        {
            return true;
        }

        if (line[0]=='/' && line[1]=='*')
        {
            return true;
        }

        if (line[0]==' ' && line[1]=='*')
        {
            return true;
        }

        return false;
    }


    // Source :  https://devqa.io/extract-numbers-string-java-regular-expressions/
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
