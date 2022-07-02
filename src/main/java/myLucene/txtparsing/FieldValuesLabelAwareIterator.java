package myLucene.txtparsing;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.documentiterator.LabelsSource;

import javax.imageio.IIOException;
import javax.print.Doc;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;

public class FieldValuesLabelAwareIterator implements LabelAwareIterator {

    private final IndexReader reader;
    private final String field;
    private int currentId = 0;


    public FieldValuesLabelAwareIterator(String field, IndexReader reader){
        this.field = field;
        this.reader = reader;
    }

    @Override
    public boolean hasNextDocument() {
        return currentId < reader.numDocs();
    }

    @Override
    public LabelledDocument nextDocument() {
        if (!hasNextDocument()){
            return null;
        }
        try {
            Document document = reader.document(currentId, Collections.singleton(field));
            LabelledDocument labelledDocument = new LabelledDocument();
            labelledDocument.addLabel("doc_" + currentId);
            labelledDocument.setContent(document.getField(field).stringValue());
            return labelledDocument;
        } catch (IOException e){
            throw new RuntimeException(e);
        } finally {
            currentId++;
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public LabelsSource getLabelsSource() {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public LabelledDocument next() {
        return null;
    }
}
