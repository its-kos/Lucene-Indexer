package myLucene.app;

import myLucene.utils.IO;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;

import java.io.File;
import java.util.Arrays;

public class LuceneApp {
    public static void main(String args[]){
        try {
            File f = new File("/IR2022/Results/results-k20.txt");
            f.delete();
            f = new File("/IR2022/Results/results-k30.txt");
            f.delete();
            f = new File("/IR2022/Results/results-k50.txt");
            f.delete();

            ParagraphVectors paragraphVectors = Indexer.index();
            String txt_file = IO.ReadEntireFileIntoAString("IR2022/queries.txt");
            String[] temp = txt_file.split("///");
            String[] queries = Arrays.copyOf(temp, temp.length - 1);
            System.out.println("Read: "+ queries.length + " queries");

            int[] Ks = new int[] {20, 30, 50};

            for (int k:Ks){
                for (String query:queries) {
                    String queryId = query.trim().split("\n")[0].trim();
                    String text = query.trim().split("\n")[1].trim().replace("\n", "");

                    /* ΦΑΣΗ 3 */
                    //IndexSearcher.search(text, k, queryId);

                    /* ΦΑΣΗ 4 */
                    IndexSearcher.searchND4J(text, queryId, k, paragraphVectors);
                }
            }
            System.out.println("Written in txt files");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
