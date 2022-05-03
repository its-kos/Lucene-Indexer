package app;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;

public class IndexSearcher {
    private static String indexLocation = ("index");
    private static String field = "contents";
    public static TopDocs search(String query, int k){
        try{
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation)));
            org.apache.lucene.search.IndexSearcher indexSearcher = new org.apache.lucene.search.IndexSearcher(indexReader);

            //indexSearcher.setSimilarity(new BM25Similarity());
            indexSearcher.setSimilarity(new ClassicSimilarity());

            // define which analyzer to use for the normalization of user's query
            Analyzer analyzer = new EnglishAnalyzer();
            //Analyzer analyzer = new StandardAnalyzer();
            //Analyzer analyzer = new SimpleAnalyzer();

            // create a query parser on the field "contents"
            QueryParser parser = new QueryParser(field, analyzer);

            Query res = parser.parse(QueryParser.escape(String.valueOf(query)));
            System.out.println("Searching for: " + res.toString(field));

            // search the index using the indexSearcher
            TopDocs results = indexSearcher.search(res, k);

            ScoreDoc[] hits = results.scoreDocs;
            //Close indexReader
            indexReader.close();

            return results;

        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static Document getId(int id) throws IOException {
        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation)));
        org.apache.lucene.search.IndexSearcher indexSearcher = new org.apache.lucene.search.IndexSearcher(indexReader);
        Document doc = indexSearcher.doc(id);
        indexReader.close();
        return doc;
    }
}
