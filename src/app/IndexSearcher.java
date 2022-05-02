package app;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

public class IndexSearcher {
    public static TopDocs search(org.apache.lucene.search.IndexSearcher query, int k){
        try{
            String indexLocation = ("index"); //define where the index is stored
            String field = "contents"; //define which field will be searched

            //Access the index using indexReaderFSDirectory.open(Paths.get(index))
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation))); //IndexReader is an abstract class, providing an interface for accessing an index.

            //Creates a searcher searching the provided index, Implements search over a single IndexReader.
            org.apache.lucene.search.IndexSearcher indexSearcher = new org.apache.lucene.search.IndexSearcher(indexReader);

            //indexSearcher.setSimilarity(new BM25Similarity());
            indexSearcher.setSimilarity(new ClassicSimilarity());

            // define which analyzer to use for the normalization of user's query

            Analyzer analyzer = new EnglishAnalyzer();
            //Analyzer analyzer = new StandardAnalyzer();
            //Analyzer analyzer = new SimpleAnalyzer();
            //Analyzer analyzer = new KeywordAnalyzer();

            // create a query parser on the field "contents"
            QueryParser parser = new QueryParser(field, analyzer);

            Query res = parser.parse(QueryParser.escape(String.valueOf(query)));
            System.out.println("Searching for: " + res.toString(field));

            // search the index using the indexSearcher
            TopDocs results = indexSearcher.search(res, k);

            ScoreDoc[] hits = results.scoreDocs;
            System.out.println("Test: " + hits[0]);
            //Close indexReader
            indexReader.close();

            return results;

        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
