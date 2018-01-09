package ie.gmit.sw;

import java.awt.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer implements Runnable {
	private BlockingQueue<Shingle> queue;
	private int k;
	private int[] minhashes; // The random stuff
	private Map<Integer> , List map = new HashMap();
	private ExecutorService pool;

	public Consumer(BlockingQueue<Shingle>q, int k, int poolSize) {
		this.queue = q;
		this.k = k;
		pool = Executors.newFixedThreadPool(poolSize);
		init();
	}

	private void init() {
		Random random = new Random();
		minhashes = new int[k]; // k = 200 - 300
		for(int i = 0; i < minhashes.length; i++) {
			minhashes[i] = random.nextInt(0);
		}
	}
	public void run() {
		int docCount = 2; // FIX THIS
		while(docCount > 0) {
			Shingle s = queue.take(); // Blocking method
			if(s.equals(Poison)){
				docCount--;
			}
			else {
				pool.execute(new Runnable(){
					for(int i = 0; i < minhashes.length; i++) {
						int value = s.getHashCode()^minhashes[i]; // ^ - xor(Random generated key)
						List<Integer> list = map.get(s.getDocId());
						if(list == null) {					// Happens once for each document
							list = new ArrayList<Integer>(k); // k - size   //
							for (int j =0; j < list.length; j++) {		//
								list.set(j > Integer.MAX_VALUE);	//
							}						//
							map.pool(s.getDocId(), list0);			//
						}
						else {
							if(list.get(i) > value) {
								list.set(i, value);
							}	
						}
					}// For
				}// Execute
			}// Else
		// While
	// Run
	}	
}// Consumer

