<<<<<<< HEAD
package myLucene.txtparsing;
=======
package main.java.myLucene.txtparsing;
>>>>>>> f9ca8a0438de0104c61a6fd513191e7b316121f3

public class MyDoc {

    private int docid;
    private String content;

    public MyDoc(int docid, String content) {
        this.docid = docid;
        this.content = content;
    }

    @Override
    public String toString() {
        return "MyDoc: {" + "\n\tDocId: " + docid + "\n\tContent: " + content + "\n}";
    }

    // ---- Getters & Setters definition ---- //

    public int getDocid() {
        return docid;
    }

    public void setDocid(int id) {
        this.docid = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
