package app;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;

public class IndexSearcher {
    private static final String indexLocation = ("index");
    private static final String field = "contents";


    public static TopDocs search(String query, int k){
        try{
            IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Paths.get(indexLocation)));
            org.apache.lucene.search.IndexSearcher indexSearcher = new org.apache.lucene.search.IndexSearcher(indexReader);


            Analyzer analyzer = new StandardAnalyzer();
            MoreLikeThis mlt = new MoreLikeThis(indexReader);
            mlt.setMinTermFreq(0);
            mlt.setMinDocFreq(0);
            mlt.setFieldNames(new String[]{"docid", "content"});
            mlt.setAnalyzer(analyzer);

            Reader sReader = new StringReader(query);
//            Query query = mlt.like(sReader, null);

            Query res = mlt.like(sReader);
            indexSearcher.setSimilarity(new ClassicSimilarity());
            TopDocs results = indexSearcher.search(res, k);

            for ( ScoreDoc scoreDoc : results.scoreDocs ) {
                Document aSimilar = indexSearcher.doc( scoreDoc.doc );
                String similarTitle = aSimilar.get("docid");
                String similarContent = aSimilar.get("content");

                System.out.println("====similar finded====");
                System.out.println("title: "+ similarTitle);
                System.out.println("content: "+ similarContent);
            }

//            indexSearcher.setSimilarity(new ClassicSimilarity());
//
//            // create a query parser on the field "contents"
//            QueryParser parser = new QueryParser(field, analyzer);
//
//            Query res = parser.parse(QueryParser.escape(String.valueOf(query)));
//            System.out.println("Searching for: " + res.toString(field));
//
//            // search the index using the indexSearcher
//            TopDocs results = indexSearcher.search(res, k);

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
