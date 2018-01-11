//A Launcher class which starts the worker threads and performs Jaccard Indexing.
//Author: Danielis Joniskis

package ie.gmit.sw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Launcher {

	private int k;
	public int shingleSize;
	private int [] minhashes;

	public Launcher(String fileA, String fileB, int hashes,int ss) throws InterruptedException {
		k = hashes;
		shingleSize = ss;
		Random random = new Random();
		
		//Filling the minhash array with random integers
		minhashes = new int [k];
		
		for (int i=0;i<minhashes.length;i++) {
			minhashes[i] = random.nextInt();
		}

		//Blocking queues 
		BlockingQueue <Shingle> q1 = new LinkedBlockingQueue<>();
		BlockingQueue <Shingle> q2 = new LinkedBlockingQueue<>();
		
		//Hash maps
		Map <Integer,List<Integer>> m1 = new HashMap<>();
		Map <Integer,List<Integer>> m2 = new HashMap<>();
		
		//Worker threads for both File A and File B
		Thread t1 = new Thread(new DocumentParser(fileA,q1,5,k),"T1");
		Thread t2 = new Thread(new DocumentParser(fileB,q2,5,k),"T2");
		
		t1.start();
		t2.start();

		t1.join();
		t2.join();

		//Consumer threads creating number of minhashes
		Thread t3 = new Thread(new Consumer(q1,m1,k,minhashes),"T3");
		Thread t4 = new Thread(new Consumer(q2,m2,k,minhashes),"T4");
		
		t3.start();
		t4.start();
		
		t3.join();
		t4.join();

		//Performing Jaccard Indexing on the two hash maps
		float similarity = Jaccard(m1.get(0),m2.get(0));
		
		System.out.println("Files A and B have a similarity of : "+similarity+"%");		
	}
	
	float Jaccard(List<Integer> a,List<Integer> b) {
		
		float similarity = 0.0f;
		List<Integer> intersection = new ArrayList<Integer>(a);
		
		intersection.retainAll(b);
		similarity = ((float)intersection.size()/k)*100;
		
		return similarity;
	}
}