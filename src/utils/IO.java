package utils;

import java.io.*;
import java.util.Scanner;

public class IO {

    public static File[] listFiles(String directoryName) {
        File directory = new File(directoryName);
        return directory.listFiles();
    }
    public static void printFiles(String directoryName) {
        File directory = new File(directoryName);
        for(String str: directory.list()){
            System.out.println(str);
        }
    }

    public static String ReadEntireFileIntoAString(String file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));
        scanner.useDelimiter("\\A"); //\\A stands for :start of a string
        String entireFileText = scanner.next();
        return entireFileText;
    }

    public static StringBuffer ReadFileIntoAStringLineByLine(String file) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        StringBuffer stringBuffer = new StringBuffer();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuffer.append(line).append("\n");
        }
        return stringBuffer;
    }

    public String ReadEntireFileIntoAStringCharByChar(String file) throws IOException {

        FileReader fileReader = new FileReader(file);

        String fileContents = "";

        int i;

        while ((i = fileReader.read()) != -1) {
            char ch = (char) i;

            fileContents = fileContents + ch;
        }
        return fileContents;
    }
}