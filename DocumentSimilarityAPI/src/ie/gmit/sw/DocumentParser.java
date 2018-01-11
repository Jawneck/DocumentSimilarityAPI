//A DocumentParser class which parses the files for comparison.
//Author: Danielis Joniskis

package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;

public class DocumentParser implements Runnable {
	
	private BlockingQueue <Shingle> queue;
	private String file;
	private int shingleSize,k;
	private Deque<String>buffer=new LinkedList<>();
	private int docId;	

	public DocumentParser(String file,BlockingQueue<Shingle>q, int shingleSize, int k) {
		this.queue = q;
		this.file = file;
		this.shingleSize = shingleSize;
		this.k = k;
	}

	public void run() {
		BufferedReader br = null;
		try {
			//Reading in the file
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			//Reading through every line
			while((line = br.readLine())!=null){
				String uLine= line.toUpperCase();
				//Splitting the lines into words where a space is present
				String [] words = uLine.split(" ");
				//Adding words to buffer to create the shingle and then add the shingles to the queue
				addWordsToBuffer(words);
				Shingle s = getNextShingle();
				if(s == null) {
					continue;
				}
				queue.put(s);
			}
			
			// write End of File shingle - used by Consumer class
			queue.put(new Shingle(0,0));
			
			flushBuffer();
			br.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}	
	}
	
	//A simple method which adds words to the string buffer
	private void addWordsToBuffer(String[] words) {
		for(String s: words) {
			buffer.add(s);
		}
	}

	//A method which gets the next shingle
	private Shingle getNextShingle() {
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		while(counter < shingleSize) {
			if(buffer.peek()!=null) 
				sb.append(buffer.poll());
			counter++;
		}
		if(sb.length()>0) {
			int docId=0;
			return (new Shingle(docId,sb.toString().hashCode()));
		}
		else {
			return null;
		}
	}
	
	//A method which flushes all remaining bytes
	private void flushBuffer() throws InterruptedException {
		while(buffer.size()>0) {
			Shingle s =getNextShingle();
			if(s != null) {
				queue.put(s);	
			}else {
				queue.put(new Poison(0,0));
			}
		}
	}
}