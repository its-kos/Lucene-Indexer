package app;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class IndexSearcher {
    private static final String indexLocation = ("index");
    private static final String field = "contents";
    private static Document document;

    public static void search(String query, int k, String queryId) {

        String format = String.format("%02d", Integer.parseInt(queryId.substring(1)));

        try {
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation)));
            org.apache.lucene.search.IndexSearcher indexSearcher = new org.apache.lucene.search.IndexSearcher(indexReader);

            Analyzer analyzer = new MyAnalyzer();
            MoreLikeThis mlt = new MoreLikeThis(indexReader);
            mlt.setAnalyzer(analyzer);

            indexSearcher.setSimilarity(new ClassicSimilarity());

            QueryParser parser = new QueryParser(field, analyzer);
            Query res = parser.parse(QueryParser.escape(String.valueOf(query)));
            System.out.println("Searching for: " + res.toString(field));

            TopDocs results = indexSearcher.search(res, k);

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(".\\IR2022\\results\\results-k" + k + ".txt"), StandardCharsets.UTF_8))) {
                for (int i = 0; i < results.scoreDocs.length; i++) {
                    document = indexReader.document(results.scoreDocs[i].doc);
                    ScoreDoc scoreDoc = results.scoreDocs[i];

                    //System.out.println(format);

                    // Write the results of the initial query
                    writer.write("Q" + format + " Q0 " + document.get("docid") + " 0 " + scoreDoc.score + " myRun" + "\n");

                    // Passing the results to MLT to get 5 more recommended documents
                    Query simQuery = mlt.like(field, new StringReader(query));
                    TopDocs related = indexSearcher.search(simQuery, 5);

                    for (ScoreDoc rd : related.scoreDocs){
                        document = indexReader.document(rd.doc);
                        // Write the results of the documents returned from MLT
                        writer.write("Q" + format + " Q0 " + document.get("docid") + " 0 " + rd.score + " myRun" + "\n");
                    }
                }
            }
            indexReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}