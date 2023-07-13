import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class JavaFiles {
    private ArrayList<JavaFile> list;

    /**
     * We create an arraylist of JavaFile Object.
     * @param size is the intial size of the arraylist
     */
    public JavaFiles(int size){
        this.list = new ArrayList<JavaFile>(size);
    }

    public void reset(){
        this.list = new ArrayList<JavaFile>(0);
    }

    public void setList(ArrayList<JavaFile> newList){
        this.list = newList;
    }

    /**
     * Open the folder and use getFolderFiles function to find all the java files and folder inside (see getFolderFiles)
     * @param path the path of the folder we want to excute jls program on
     */
    public void jls(String path){
        File folder = new File(path);
        getFolderFiles(folder);
    }

    /**
     * This function is the main body of Jls.
     * This function uses recursion. We open every sub-folder. In every folder, we look for java files and add them to
     * our list. We find all needed information using folder location or by opening the folder looking for words.
     * @param folder
     */
    public void getFolderFiles(File folder){

        int testI = 0;
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {                      // Recursive case : Open subfolder
                getFolderFiles(fileEntry);
            } else {                                            // Basic case     : Add each file information to result

                // find last "." index to substring file extension to find java files
                int index = fileEntry.getName().lastIndexOf('.');
                if(index > 0) {
                    String extension = fileEntry.getName().substring(index + 1);
                    if (extension.equals("java")){

                        JavaFile tempJavaFile = new JavaFile(fileEntry.getAbsolutePath(), "",
                                fileEntry.getName().substring(0, index), -1,-1);
                        String packageLine = tempJavaFile.findCodeLineOfWord("package");
                        if (packageLine != ""){
                            tempJavaFile.setPackageName(packageLine.substring(8, packageLine.length()-1));}

                        this.list.add(tempJavaFile);

                    }
                }
            }
        }
    }


    /**
     * This function uses nvloc() function found in JavaFile.java. Every file of our this.list, will run nvloc() on
     * its self. nvloc() opens the file and count (not empty) lines.
     * @throws FileNotFoundException
     */
    public void nvloc() throws FileNotFoundException {
        for (JavaFile jf : this.list) {
            jf.nvloc();
        }
    }

    public void commentsDensityProject() throws FileNotFoundException {
        for (JavaFile jf : this.list) {
            double tempCd = jf.commentDensity();
            System.out.println(jf.getName() + " : " + tempCd);
        }
    }
    /**
     * This function will open every file in this.list . Then, for every file, its will try to find the name of the
     * other class (inside this.list). When an occurance is found, we add it to both java files.
     * Then we uptade the coupling count of both java files. Of course, we don't look to find the name of a class in
     * the same class or if its not the same folder.
     */
    public void Icsec(){

        for (JavaFile javaFileX : this.list) {

            for (JavaFile javaFileY : this.list){


                if ((javaFileX.getName() != javaFileY.getName()) &&
                        (javaFileY.getPackageName().contains(javaFileX.getPackageName()))){

                    if ( javaFileX.findCodeLineOfWord(javaFileY.getName()) != ""){
                        javaFileX.addToCouplingList(javaFileY.getName()); // Couplage Interne
                        javaFileY.addToCouplingList(javaFileX.getName()); // Couplage Externe
                        javaFileY.uptadeCoupling();
                        javaFileX.uptadeCoupling();
                    }
                }
            }

        }
    }

    /**
     * This function first sorts against Icsec and nvloc. Then, it returns a number of divine classes depending
     * on the threshold.
     * @param treshold will determine how many devine classes will be returned.
     * @return
     */
    public ArrayList<JavaFile> egon(double treshold){

        // Sorting myList entries by NVLOC
        List<JavaFile> sortedListByNvloc = new ArrayList<JavaFile>(list);
        Collections.sort(sortedListByNvloc, new SortByNvloc());

        // Sorting myList entries by CSEC
        List<JavaFile> sortedListByCsec = new ArrayList<JavaFile>(list);
        Collections.sort(sortedListByCsec, new SortByCsec());

        int numberOfJavaClas = (int) ((int)list.size()*  treshold);

        List<JavaFile> resultNvolc  = sortedListByNvloc.subList(0, numberOfJavaClas);
        List<JavaFile> resultCsec  = sortedListByCsec.subList(0, numberOfJavaClas);

        // Find the common elements
        resultNvolc.retainAll(resultCsec);
        ArrayList<JavaFile> egonResult = new ArrayList<JavaFile>(resultNvolc);

        //return resultNvolc;
        return egonResult;
    }
    /**
     * This function can read any csv and convert it to this.list
     * This function is not supposed to be used on a file where nvloc is used before Icsec.
     * @param filePath We read the csv found in this path
     * @throws IOException
     */
    public void readFromCsv(String filePath) throws IOException {
        String line = null;
        BufferedReader stream = null;
        ArrayList<JavaFile> csvList = new ArrayList<JavaFile>();

        try {
            stream = new BufferedReader(new FileReader(filePath));
            while ((line = stream.readLine()) != null) {
                String[] splitted = line.split(",");

                int IcsecCount = -1;
                int egonCount = -1;

                if (splitted.length == 4){
                    IcsecCount = Integer.parseInt(splitted[3]);
                }
                if (splitted.length == 5){
                    egonCount = Integer.parseInt(splitted[4]);
                }

                JavaFile temp = new JavaFile(splitted[0],splitted[1],splitted[2], IcsecCount , egonCount);

                csvList.add(temp);
            }
        } finally {
            if (stream != null)
                stream.close();
        }

        this.list = csvList;
    }

    /**
     * Read every JavaFile.java in this.list. Write its information in a line that will be written later on the csv.
     * @param filePath is where we will write the file
     * @param writeConsole Do we write the data that has been written in the file in console too?
     * @return the written file itself.
     * @throws IOException
     */
    public File writeDataToCsv(String filePath, boolean writeConsole) throws IOException {
        File csvFile = new File(filePath);
        FileWriter fileWriter = new FileWriter(csvFile);

        for (JavaFile jf : this.list) {

            StringBuilder line = new StringBuilder();

            line.append(jf.getLocation() + ",");

            line.append(jf.getPackageName() + ",");

            line.append(jf.getName());

            if (jf.getCoupling() != -1) {
                line.append("," + jf.getCoupling());

            }
            if (jf.getLines() != -1) {
                line.append("," + jf.getLines());
            }
            line.append("\n");
            fileWriter.write(line.toString());

            if (writeConsole){
                System.out.print(line);
            }

        }
        System.out.println("Csv file written in : " + csvFile.getAbsolutePath());
        fileWriter.close();
        return csvFile;
    }
}