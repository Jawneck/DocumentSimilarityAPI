package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class DocumentParser implements Runnable{
	private BlockingQueue<Shingle>queue;
	private String file;
	private int shignleSize, k;
	private Deque<String> buffer = new LinkedList<>();
	private int docId;	

	public DocumentParser(String file, BlockingQueue<Shingle>q, int shingleSize, int k) {
		this.queue = q;
	}
	
	public void run() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file))
);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String line = null;
		try {
			while((line = br.readLine())!= null) {
				String uLine = line.toUpperCase();
				String[] words = uLine.split(" "); // Can also take a regexpression
				addWordsToBuffer(words);
				Shingle s = getNextShingle();
				queue.put(s); // Blocking method. Add is not a blocking method
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			flushBuffer();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}// Run


	private void addWordsToBuffer(String [] words) {
		for(String s : words) {
			buffer.add(s);
		}
  
        }

  	private Shingle getNextShingle() {
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		int shingleSize = 3;
		while(counter < shingleSize ) {
			if(buffer.peek() != null) {
				sb.append(buffer.poll());
				counter++;
			}
		}  
		if (sb.length() > 0) {
			return(new Shingle(docId, sb.toString().hashCode()));
		}
		else {
			return(null);
		}
  	} // Next shingle
	

	private void flushBuffer() throws InterruptedException {
		while(buffer.size() > 0) {
			Shingle s = new Shingle();
			s = getNextShingle();
			if(s != null) {
				queue.put(s);
			}
			else {
				queue.put(new Poison(docId, 0));
			}
		}
	}

        
  }// Document Parser
