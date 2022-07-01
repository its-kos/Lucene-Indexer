<<<<<<<< HEAD:src/main/java/myLucene/app/MyAnalyzer.java
package myLucene.app;
========
package main.java.myLucene.txtparsing;
>>>>>>>> f9ca8a0438de0104c61a6fd513191e7b316121f3:src/main/java/myLucene/txtparsing/MyAnalyzer.java

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
public class MyAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        StandardTokenizer myTokenizer = new StandardTokenizer();
        TokenFilter filter = new StandardFilter(myTokenizer);
        filter = new StopFilter(filter, StopAnalyzer.ENGLISH_STOP_WORDS_SET);
        filter = new LowerCaseFilter(filter);
        filter = new PorterStemFilter(filter);
        return new TokenStreamComponents(myTokenizer, filter);
    }
}