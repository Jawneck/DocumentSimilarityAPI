//A Consumer class which contains methods on populating the array with number of hashes
//Author: Danielis Joniskis

package ie.gmit.sw;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable{

	private BlockingQueue<Shingle> queue;
	private int k;
	private int [] minhashes;
	private Map <Integer,List<Integer>> map;	
	
	public Consumer(BlockingQueue<Shingle> q, Map <Integer,List<Integer>> map,int k,int[] minhashes) {
		super();
		this.queue = q;
		this.k = k;
		this.map = map;
		this.minhashes = minhashes;
	}//constructor
	
	//An init method which creates a random integer to fill the minhash array
	public void init(){
		Random random = new Random();
		minhashes = new int[k];
		
		for(int i=0; i < minhashes.length; i++){
			minhashes[i] = random.nextInt();
		}//for
	}//init
	
	//A run method which populates the array list with the number of hashes 
	public void run() {
		int docCount = 1;
		int max = Integer.MAX_VALUE;
		int value = 0;
		
		while(docCount>0) {
			try {
				//Retrieving the head of the queue with take
				Shingle s = queue.take();
				
				if(s.getHashcode()==0) {
					docCount--;
					continue;
				}//if

				//Returning the value to which the key is mapped
				List<Integer> list = map.get(s.getDocId());
				
				if(list == null) {
					list = new ArrayList<Integer>(k);
					for(int i = 0; i <k;i++) {
						list.add(i, max);
					}//for
					
					//value of list associated with key shingle doc id
					map.put(s.getDocId(), list);	
				}//if
				
				for(int i = 0;i<minhashes.length;i++) {
					
					value =s.getHashcode() ^ minhashes[i];
									
					if(list.get(i) > value) {
						list.set(i, value);
					}//if
				}//for		
			}//try
			
			 catch (InterruptedException e) {
				 
				e.printStackTrace();
			}
			
		}//while
	}//run
}//Consumer