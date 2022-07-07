## Information Retrieval
This program creates a Lucene index and tries to retrieve documents relevant to 10 queries. 3 ways are tried as shown below.
# Data
The data is a collection of 18316 texts which are separated with the '///' symbol. Each text includes an id assigned to it separated with a '\n'. The data is located in the IR2022/documents.txt file. The queries used to retrieve the documents are located in the IR2022/queries.txt file.
# Index creation
Lucene creates an index using the analyzer you provide it. We opted to index both the document id and the text but we only store the document id as the text doesn't need to be retrieved.
# Phase 1
Baseline Document Retrieval using EnglishAnalyzer and ClassicSimilarity. This is the standard Lucene way of retrieving documents
# Phase 2
Document Retrieval using MoreLikeThis library whicih retrieves the k most similar texts on top of the returned ones (kind of like youtube's recommendation system).
# Phase 3
Using the DL4J (Deep learning for Java) library, we create embeddings of the texts and the queries and retrieve the most similar ones based on the cosine similarity of the text vectors.
# Evaluating results
To evaluate our results we use the tool "trec_eval". Using this tool and a "baseline" of correct answers (file IR2022/qrels.txt) we can calculate several metrics like MAP, Precision@K (k = 1, 2, 3, ...) and many more.
# Running the program.
To run the program, simply create a configuration, pointing it to the main function of the "LuceneApp" class. Then, depending on which method of retrieval you want, uncomment the appropriate function.
