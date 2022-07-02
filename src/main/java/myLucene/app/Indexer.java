package myLucene.app;

// tested for lucene 7.7.3 and jdk18

import myLucene.txtparsing.FieldValuesLabelAwareIterator;
import myLucene.txtparsing.MyDoc;
import myLucene.txtparsing.TXTParsing;
import myLucene.utils.IO;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.deeplearning4j.models.embeddings.learning.impl.elements.CBOW;
import org.deeplearning4j.models.embeddings.learning.impl.elements.SkipGram;
import org.deeplearning4j.models.embeddings.learning.impl.sequence.DM;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.sequencevectors.interfaces.SequenceIterator;
import org.deeplearning4j.models.sequencevectors.sequence.Sequence;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.documentiterator.DocumentIterator;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.documentiterator.SimpleLabelAwareIterator;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Indexer {
    static int it = 0;
    public static ParagraphVectors index() throws Exception{

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

            IndexReader reader = DirectoryReader.open(indexWriter);
            FieldValuesLabelAwareIterator iterator = new FieldValuesLabelAwareIterator("content", reader);
            ParagraphVectors paragraphVectors = new ParagraphVectors.Builder()
                    .iterate(iterator)
                    .tokenizerFactory(new DefaultTokenizerFactory())
                    .build();
            paragraphVectors.fit();

            indexWriter.close();

            Date end = new Date();
            System.out.println("Index created...");
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");

            System.out.println(paragraphVectors);
            return paragraphVectors;

        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() + "\n with message: " + e.getMessage());
            return null;
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
