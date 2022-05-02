package app;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
import utils.IO;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;

import static app.IndexSearcher.search;

public class LuceneApp {
    public static void main(String args[]){
        try {
            Indexer.index();
            String txt_file = IO.ReadEntireFileIntoAString(".\\IR2022\\queries.txt");
            String[] temp = txt_file.split("///");
            String[] queries = Arrays.copyOf(temp, temp.length - 1);
            System.out.println("Read: "+ queries.length + " queries");

            String indexLocation = ("index");
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation)));
            org.apache.lucene.search.IndexSearcher indexSearcher = new org.apache.lucene.search.IndexSearcher(indexReader);
            indexSearcher.setSimilarity(new ClassicSimilarity());

            int[] Ks = new int[] {20, 30, 50};

            for (int k:Ks){
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(".\\IR2022\\results\\results-k" + k + ".txt"), StandardCharsets.UTF_8))) {
                    for (String query:queries) {
                        String queryId = query.trim().split("\n")[0].trim();
                        String text = query.trim().split("\n")[1].trim();
                        TopDocs results = search(indexSearcher, k);
                        ScoreDoc[] hits = results.scoreDocs;
                        System.out.println(hits.length);

                        for (ScoreDoc hit: hits) {
                            Document hitdoc = indexSearcher.doc(hit.doc);
                            //System.out.println(hitdoc.toString());
                            writer.write("Q" + String.format("%02d", Integer.parseInt(queryId.substring(1))) + " Q0 " + hitdoc.get("docid") + " 0 " + hit.score + " myRun" + "\n");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
