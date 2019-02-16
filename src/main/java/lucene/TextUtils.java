package lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.IssueModel;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;

public class TextUtils {
	
	public TextUtils() {
		
	}
	
	public List<IssueModel> getSimilarResults(String newIssue, List<IssueModel> issues) {
		RAMDirectory ramDir = new RAMDirectory();
		Analyzer analyzer = new StandardAnalyzer();

		writeIndex(ramDir, analyzer, issues);
		return searchIndex(ramDir, analyzer, newIssue);
	}

	private void writeIndex(RAMDirectory ramDir, Analyzer analyzer, List<IssueModel> models) {
		try {
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			iwc.setOpenMode(OpenMode.CREATE);
			IndexWriter writer = new IndexWriter(ramDir, iwc);

			for(IssueModel m : models) {
				System.out.println("Writing " + m.title + " " + m.body);
				indexDoc(writer, m.title, m.body, m.URL);
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void indexDoc(IndexWriter writer, String name, String content, String URL) throws IOException {
		Document doc = new Document();
		doc.add(new TextField("name", name, Store.YES));
		doc.add(new TextField("content", content, Store.YES));
		doc.add(new TextField("URL", URL, Store.YES));
		writer.addDocument(doc);
	}

	private List<IssueModel> searchIndex(RAMDirectory ramDir, Analyzer analyzer, String q) {
		IndexReader reader = null;
		List<IssueModel> ret = new ArrayList<IssueModel>();
		try {
			reader = DirectoryReader.open(ramDir);
			IndexSearcher searcher = new IndexSearcher(reader);

			QueryParser qp = new QueryParser("content", analyzer);
			Query query = qp.parse(q);
			System.out.println("Searching for\t" + q);

			TopDocs foundDocs = searcher.search(query, 10);
			System.out.println("Total Results : " + foundDocs.totalHits);

			for (ScoreDoc sd : foundDocs.scoreDocs) {
				Document d = searcher.doc(sd.doc);
				ret.add(new IssueModel(d.get("URL"), d.get("name"), d.get("content")));
				
				System.out.println("Document Name : " + d.get("name") + ", Content : " + d.get("content")
						+ "\nScore : " + sd.score);
			}

			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
}