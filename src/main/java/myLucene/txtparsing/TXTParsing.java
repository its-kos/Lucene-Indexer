package myLucene.txtparsing;

import myLucene.utils.IO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TXTParsing {
    public static List<MyDoc> parse(String file) {
        try{
            //Parse txt file
            String txt_file = IO.ReadEntireFileIntoAString(file);
            String[] docs = txt_file.split("///");
            System.out.println("Read: "+ docs.length + " docs");

            //Parse each document from the txt file
            List<MyDoc> parsed_docs = new ArrayList<MyDoc>();
            for (String doc:docs){
                int docId = Integer.parseInt(doc.trim().split("\n")[0].trim());
                String txtRest = doc.trim().split("\n")[1].trim();
                MyDoc mydoc = new MyDoc(docId, txtRest);
                parsed_docs.add(mydoc);
            }
            return parsed_docs;
        } catch (Throwable err) {
            err.printStackTrace();
            return null;
        }
    }
}
