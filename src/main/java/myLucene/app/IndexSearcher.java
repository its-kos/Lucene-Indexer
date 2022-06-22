package main.java.myLucene.app;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
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
import java.util.ArrayList;

public class IndexSearcher {
    private static final String indexLocation = ("index");
    private static final String field = "contents";
    public static void search(String query, int k, String queryId) {

        String format = String.format("%02d", Integer.parseInt(queryId.substring(1)));

        try {
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation)));
            org.apache.lucene.search.IndexSearcher indexSearcher = new org.apache.lucene.search.IndexSearcher(indexReader);

            Analyzer analyzer = new EnglishAnalyzer();
            MoreLikeThis mlt = new MoreLikeThis(indexReader);
//            Sets the frequency below which terms will be ignored in the source doc.
            mlt.setMinTermFreq(1);
//            Sets the frequency at which words will be ignored which do not occur in at least this many docs.
            mlt.setMinDocFreq(1);
            mlt.setFieldNames(new String[]{"docid", "contents"});
            mlt.setAnalyzer(analyzer);


            indexSearcher.setSimilarity(new ClassicSimilarity());

            QueryParser parser = new QueryParser(field, analyzer);
            Query res = parser.parse(QueryParser.escape(String.valueOf(query)));
            System.out.println("Searching for: " + res.toString(field));

            TopDocs results = indexSearcher.search(res, k);

            FileWriter fileWriter = new FileWriter(".\\IR2022\\results\\results-k" + k + ".txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            ArrayList includedDocs = new ArrayList();


            for (int i = 1; i < results.scoreDocs.length; i++) {

                Document document = indexReader.document(results.scoreDocs[i].doc);
                ScoreDoc scoreDoc = results.scoreDocs[i];
                includedDocs.add(Integer.parseInt(document.get("docid")));

                // Write the results of the initial query
                bufferedWriter.write("Q" + format + " Q0 " + document.get("docid") + " 0 " + scoreDoc.score + " myRun" + "\n");
            }

            // Passing the results to MLT to get 5 more recommended documents
            Query simQuery = mlt.like(field, new StringReader(query));
            TopDocs related = indexSearcher.search(simQuery, k+5);
            for (ScoreDoc rd : related.scoreDocs){
                Document document = indexReader.document(rd.doc);
                if (!includedDocs.contains(Integer.parseInt(document.get("docid")))){
                    // Write the results of the documents returned from MLT
                    bufferedWriter.write("Q" + format + " Q0 " + document.get("docid") + " 0 " + rd.score + " myRun" + "\n");
                }
            }

            bufferedWriter.close();
            indexReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}