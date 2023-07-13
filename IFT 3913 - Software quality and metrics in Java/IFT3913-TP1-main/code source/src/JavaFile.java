import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class JavaFile {
    private String location;
    private String packageName;
    private String name;
    private int coupling;
    private int lines;
    private ArrayList<String> couplingList = new ArrayList<>(0);

    private double commentsDensity = 0.0;


    // NEW
    //private double commentsDesnsity = 0.0;



    // CONSTRUCTOR
    /**
     *
     * @param location is the absolute location of the file in the hard drive
     * @param packageName is the name of the package that will be found after reading inside java file
     * @param name is the name of the file without its ext
     * @param coupling is the count of intern and extern coupling found using Icsec
     * @param lines is the count of lines found using nvloc
     */
    public JavaFile(String location, String packageName, String name, int coupling, int lines ) {
        // This constructor has one parameter, name.

        this.location = location;
        this.packageName = packageName;
        this.name = name;
        this.coupling = coupling;
        this.lines = lines;
    }


    // GETTERS/
    public String getLocation() {
        return location;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getName() {
        return name;
    }

    public int getCoupling() {
        return coupling;
    }

    public int getLines() {
        return lines;
    }

    public ArrayList<String> getCouplingList() {
        return couplingList;
    }

    // SETTERS
    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setCoupling(int coupling) {
        this.coupling = coupling;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public void setCouplingList(ArrayList<String> couplingList) {
        this.couplingList = couplingList;
    }

    public double getCommentsDensity() {
        return commentsDensity;
    }

    /**
     *
     * @param newC class name will be added to our coupling list if its not already in it
     */
    public void addToCouplingList(String newC){
        if (!inCouplingList(newC)){
            this.couplingList.add(newC);}
    }

    /**
     *
     * @param c is the name of the class we want to know if its already inside our coupling list
     * @return true if the name of the class is already in our coupling list, otherwise false
     */
    public boolean inCouplingList(String c){
        if (couplingList.size() == 0 || couplingList.indexOf(c) == -1){
            return false;
        }
        return true;
    }

    /**
     * We uptade this.coupling with the size of this.couplingList
     */
    public void uptadeCoupling(){
        this.coupling = this.couplingList.size();
    }

    /**
     *
     * @param word is a string like "package" or the name of the class to see if we can find its line inside our file
     * @return true if we find the line that is not a comment and also if we find the perfect match word.
     */
    public String findCodeLineOfWord(String word){
        try {
            File file = new File(this.location);
            Scanner scanner = new Scanner(file);

            // Read the file line by line
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;

                if(line.length() > 0 && line.contains(word)) {

                    if (!isComment(line) && isPerfectMatchString(line, word)){
                        scanner.close();
                        return line;
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return "";
    }


    static boolean isPerfectMatchString(String line, String word){
        String[] splitted = line.split("\\W");
        for (String s : splitted){
            if (word.equals(s)){
                return true;
            }
        }
        return false;
    }

    static boolean isComment(String S)
    {
        char line[] = S.toCharArray();
        if (line.length == 1){
            if(line[0]=='*'){
                return true;
            }
            return false;
        }

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

    @Override
    public String toString() {
        return " [" + packageName + ", " + name + ", " + coupling + ", " + lines + "]";
    }

    /**
     *
     * @return the count of not empty line of java file
     * @throws FileNotFoundException
     */
    public int nvloc() throws FileNotFoundException {
        int count = 0;
        File file = new File(this.location);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            if (!sc.nextLine().isEmpty())
                count++;
        }
        this.lines = count;
        sc.close();
        return count;
    }


    /**
     *
     * @return the count of not empty line of java file
     * @throws FileNotFoundException
     */
    public double commentDensity() throws FileNotFoundException {
        double allCount = 0.;
        double commentsCount = 0.0;
        File file = new File(this.location);
        Scanner sc = new Scanner(file);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (!line.isEmpty()){
               // System.out.println("line : " + line +"|");
                allCount = allCount + 1;
                if (isComment(line)){
                    commentsCount++;
                }
            }
        }
        sc.close();
        this.commentsDensity = commentsCount / allCount;
        return this.commentsDensity;
    }



}

//Helper class implementing Comparator interface
class SortByNvloc  implements Comparator<JavaFile> {
    // Method : Sorting in descending order of nvloc
    public int compare(JavaFile a, JavaFile b){
        return b.getLines() - a.getLines();
    }
}

//Helper class implementing Comparator interface
class SortByCsec implements Comparator<JavaFile> {
    //Method : Sorting in descending order of csec
    public int compare(JavaFile a, JavaFile b){
        return b.getCoupling() - a.getCoupling();
    }
}