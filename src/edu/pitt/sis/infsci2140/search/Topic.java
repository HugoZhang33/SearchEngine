package edu.pitt.sis.infsci2140.search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import edu.pitt.sis.infsci2140.analysis.TextNormalizer;
import edu.pitt.sis.infsci2140.analysis.TextTokenizer;

/**
 * A class that stores topic information.
 */
public class Topic {
    private ArrayList<char[]> titleInfo = new ArrayList<char[]>();
    private String topicId;
    
    public Topic(String topicId) {
        this.topicId = topicId;
    }
    
    public String topicId() {
        // you should implement this method
        return topicId;
    }
    
    public void setTopicId(String id) {
        this.topicId = id;
    }
    
    /**
     * Parse a list of TREC topics from the provided f.
     *
     * @param f
     * @return
     */
    public static List<Topic> parse( File f ) {
        // you should implement this method
        
        ArrayList<Topic> queryList = new ArrayList<Topic>();
        TextTokenizer tokenizer = null;
        int id = 0;
        
        // only pick up title for query
        try {
            BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream(f), "UTF-8" ) );
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( "Query.txt" ), "UTF-8" ) );
            
            String line = reader.readLine();
            
            while ( line != null ) {
                if ( line.indexOf( "<title>" ) != -1 ) { // find it, extract the content
                    int start = 7;
                    char[] content = line.substring(start).toCharArray();
                    
                    // tokenize and normalize title info
                    // add to Topic instance's titleInfo list
                    writer.write( String.valueOf(id) );
                    writer.write(' ');
                    
                    Topic topic = new Topic( String.valueOf( id++ ) );
                    tokenizer = new TextTokenizer( content );
                    char[] term = tokenizer.nextWord();
                    
                    while ( term != null ) {
                        term = TextNormalizer.normalize(term);
                        topic.setTitleInfo(term);
                        
                        writer.write(term);
                        writer.write(' ');
                        
                        term = tokenizer.nextWord();
                    }
                    queryList.add(topic);
                    writer.write('\n');
                }
                line = reader.readLine();
            }
            
            reader.close();
            writer.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return queryList;
    }
    
    public ArrayList<char[]> getTitleInfo() {
        return titleInfo;
    }
    
    public void setTitleInfo(char[] title) {
        this.titleInfo.add(title);
    }
    
}
