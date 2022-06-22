package myLucene.app;

// tested for lucene 7.7.3 and jdk18

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import myLucene.txtparsing.MyDoc;
import myLucene.txtparsing.TXTParsing;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public class Indexer {
    public static void index() throws Exception{

        // Define were to find the texts
        String txtfile =  ".\\IR2022\\documents.txt";

        // Define were to store the index
        String indexLocation = ("index");
        
        Date start = new Date();
        try {
            System.out.println("Indexing to directory: '" + indexLocation);
            Directory dir = FSDirectory.open(Paths.get(indexLocation));

            Analyzer analyzer = new EnglishAnalyzer();

            // Define retrieval model
            //Similarity similarity = new BM25Similarity();
            Similarity similarity = new ClassicSimilarity();

            // Configure IndexWriter
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setSimilarity(similarity);

            // Create a new index in the directory, removing any previously indexed documents
            iwc.setOpenMode(OpenMode.CREATE);

            // Create the IndexWriter with the configuration as above
            IndexWriter indexWriter = new IndexWriter(dir, iwc);
            
            // Parse txt document using TXT parser and index it
            List<MyDoc> docs = TXTParsing.parse(txtfile);
            for (MyDoc doc : docs){
                indexDoc(indexWriter, doc);
            }
            
            indexWriter.close();
            
            Date end = new Date();
            System.out.println("Index created...");
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");
            
        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
        }
    }

    private static void indexDoc(IndexWriter indexWriter, MyDoc mydoc){
        try {
            // make a new, empty document
            Document doc = new Document();
            
            // create the fields of the document and add them to the document
            StoredField id = new StoredField("docid", mydoc.getDocid());
            doc.add(id);
            TextField content = new TextField("contents", mydoc.getContent(), Field.Store.NO);
            doc.add(content);
            
            if (indexWriter.getConfig().getOpenMode() == OpenMode.CREATE) {
                // New index, so we just add the document (no old document can be there):
                indexWriter.addDocument(doc);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
