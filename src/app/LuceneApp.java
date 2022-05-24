package app;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import utils.IO;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static app.IndexSearcher.getId;
import static app.IndexSearcher.search;

public class LuceneApp {
    public static void main(String args[]){
        try {
            Indexer.index();
            String txt_file = IO.ReadEntireFileIntoAString(".\\IR2022\\queries.txt");
            String[] temp = txt_file.split("///");
            String[] queries = Arrays.copyOf(temp, temp.length - 1);
            System.out.println("Read: "+ queries.length + " queries");

            int[] Ks = new int[] {20, 30, 50};

            for (int k:Ks){
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(".\\IR2022\\results\\results-k" + k + ".txt"), StandardCharsets.UTF_8))) {
                    for (String query:queries) {
                        String queryId = query.trim().split("\n")[0].trim();
                        String text = query.trim().split("\n")[1].trim();
                        TopDocs results = search(text, k);
                        ScoreDoc[] hits = results.scoreDocs;
                        System.out.println(hits.length); //vgazei 0
                        for (int i = 0; i < hits.length; i++) {

                            Document hitdoc = getId(hits[i].doc);

                            writer.write("Q" + String.format("%02d", Integer.parseInt(queryId.substring(1))) + " Q0 " + hitdoc.get("docid") + " 0 " + hits[i].score + " myRun" + "\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
