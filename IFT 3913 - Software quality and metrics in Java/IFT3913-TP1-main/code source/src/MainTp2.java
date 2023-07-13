import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class MainTp2 {
    public static void main(String[] args) throws IOException {


        JavaFiles javaFiles = new JavaFiles(0);

        javaFiles.jls("C:\\Users\\leade\\Desktop\\jfreechart-master\\src\\main");
        javaFiles.commentsDensityProject();
        javaFiles.writeDataToCsv("C:\\Users\\leade\\Desktop\\tp2q1.csv", false);

        
    }
}