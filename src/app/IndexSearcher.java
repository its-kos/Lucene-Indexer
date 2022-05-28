package app;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;


import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;

public class IndexSearcher {
    private static final String indexLocation = ("index");
    private static final String field = "contents";



    public static <SearchHit> TopDocs search(String query, int k){
        try {
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation)));
            MoreLikeThis mlt = new MoreLikeThis(indexReader);
            Analyzer analyzer = new StandardAnalyzer();
            mlt.setAnalyzer(analyzer);
            org.apache.lucene.search.IndexSearcher indexSearcher = new org.apache.lucene.search.IndexSearcher(indexReader);
            indexSearcher.setSimilarity(new ClassicSimilarity());

            mlt.setMinTermFreq(0);
            mlt.setMinDocFreq(0);
            mlt.setFieldNames(new String[]{"docid", "content"});

//            String fieldName = "text";

            // create a query parser on the field "contents"
            QueryParser parser = new QueryParser(field, analyzer);
            Query res = parser.parse(QueryParser.escape(String.valueOf(query)));
            System.out.println("Searching for: " + res.toString(field));

            Query simQuery = mlt.like(field,new StringReader(query));
            // search the index using the indexSearcher
//            TopDocs results = indexSearcher.search(res, 10);
            TopDocs results = indexSearcher.search(simQuery, 10);
//            -> xamilotero map score 0.24

            for (int i=0;i<results.scoreDocs.length; i++){
                ScoreDoc scoreDoc = results.scoreDocs[i];
                Document doc = indexSearcher.doc(scoreDoc.doc);
                String docid = doc.get("docid");
                System.out.println(docid + ": " + scoreDoc.score);
//                String text = doc.get("content");
//                Query simQuery = mlt.like(field,new StringReader(text));
//                TopDocs related = indexSearcher.search(simQuery,5);
//                for (ScoreDoc sd : related.scoreDocs){
//                    Document document = indexReader.document(sd.doc);
//                    System.out.println("-> "+ document.get("docid"));
//                }

            }



//            Reader sReader = new StringReader(query);
//            Query res2 = mlt.like(field,new StringReader(query));

//            TopDocs results = indexSearcher.search(res2, k);






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
