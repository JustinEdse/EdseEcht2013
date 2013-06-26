/*
 * Indexer.java
 *
 * Created on 6 March 2006, 13:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package edse.edu.com;

import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import edse.edu.com.Hotel;
import edse.edu.com.HotelDatabase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

/**
 *
 * @author John
 */
public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
 
    private IndexWriter indexWriter = null;
    
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            indexWriter = new IndexWriter("index-directory",
                                          new StandardAnalyzer(),
                                          create);
        }
        return indexWriter;
   }    
   
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
    
    public void indexHotel(Hotel hotel) throws IOException {

        System.out.println("Indexing hotel: " + hotel);
        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();
        doc.add(new Field("id", hotel.getId(), Field.Store.YES, Field.Index.NO));
        doc.add(new Field("name", hotel.getName(), Field.Store.YES, Field.Index.TOKENIZED));
        doc.add(new Field("city", hotel.getCity(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("description", hotel.getDescription(), Field.Store.YES, Field.Index.TOKENIZED));
        String fullSearchableText = hotel.getName() + " " + hotel.getCity() + " " + hotel.getDescription();
        doc.add(new Field("content", fullSearchableText, Field.Store.NO, Field.Index.TOKENIZED));
        writer.addDocument(doc);
    }   
    
    public void rebuildIndexes() throws IOException {
          //
          // Erase existing index
          //
          getIndexWriter(true);
          //
          // Index all Accommodation entries
          //
          Hotel[] hotels = HotelDatabase.getHotels();
          for(Hotel hotel : hotels) {
              indexHotel(hotel);              
          }
          //
          // Don't forget to close the index writer when done
          //
          closeIndexWriter();
     }    
    
  
    
    
}
