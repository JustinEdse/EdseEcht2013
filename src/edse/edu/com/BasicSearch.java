package edse.edu.com;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;

public class BasicSearch
{
	private IndexSearcher searcher = null;
	private QueryParser parser = null;

	// This is the start of basic searching and can be expanded upon in
	// the near future.
	
	public BasicSearch() throws IOException
	{
		searcher = new IndexSearcher("C:/Users/User/workspace/data");
		// the index search should match keywords, and other things in the
		// .csv file from April 15th, 2013.
		// The first parameter of QueryParser is the default search field.
		// In this case I just picked time for now...
		// Make sure to use same analyzer for indexwriter and queryanalyzer!
		parser = new QueryParser("time", new StandardAnalyzer());
		
	}

	public Hits performSearch(String queryString) throws IOException,
			ParseException
	{
		//queryString could be something like: source:Android OR tweet_text:Sunny
		//There are a lot of different ways to put together your query string
		//to search for.
		Query query = parser.parse(queryString);
		Hits hits = searcher.search(query);

		return hits;
		
		//the following should be done outside of this class. This note is just
		//a reminder.
		
	}
}
