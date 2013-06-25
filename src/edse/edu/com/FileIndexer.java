package edse.edu.com;

import java.io.File;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;


public class FileIndexer
{
   //This class is to handle any Lucene manipulation
   //and other utilities with indexes, documents/CSV files.
	
	
   //Making the index 
	File indexDir = new File("C:/Users/User");
	File dataDir = new File("C:/Users/User/workspace/");
	
	String suffix = "java";
	
	FileIndexer indexer = new FileIndexer();
	
	//int numIndex = indexer.index(indexDir, dataDir, suffix);
	
	//System.out.println("Number of files indexed: " + numIndex);
	
	private int index(File indexDir, File dataDir, String suffix)throws Exception
	{
		
		IndexWriter indexWriter = new IndexWriter(
				FSDirectory.open(indexDir),
				new SimpleAnalyzer(),
				true,
				IndexWriter.MaxFieldLength.LIMITED);
		
		indexWriter.setUseCompoundFile(false);
		//STILL HAVE TO ADD THIS METHOD!
		//indexDirectory(indexWriter, dataDir, suffix);
		
		int numIndexed = indexWriter.maxDoc();
		indexWriter.optimize();
		indexWriter.close();
		
		return numIndexed;
		
	}
}
