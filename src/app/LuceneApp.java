package app;

import utils.IO;
import java.util.Arrays;

public class LuceneApp {
    private static final int[] Ks = new int[] {20, 30, 50};
    public static void main(String args[]){
        try {
            Indexer.index();
            String txt_file = IO.ReadEntireFileIntoAString(".\\IR2022\\queries.txt");
            String[] temp = txt_file.split("///");
            String[] queries = Arrays.copyOf(temp, temp.length - 1);
            System.out.println("Read: "+ queries.length + " queries");

            for (int k:Ks){
                for (String query:queries) {
                    String queryId = query.trim().split("\n")[0].trim();
                    String text = query.trim().split("\n")[1].trim();
                    IndexSearcher.search(text, k, queryId);
                }
            }
            System.out.println("Written in txt files");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
