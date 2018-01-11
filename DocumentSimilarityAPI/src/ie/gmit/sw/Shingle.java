//A Shingle class containing the Shingle object.
//Author: Danielis Joniskis

package ie.gmit.sw;

public class Shingle {
	private int docId;
	private int hashcode;
	//Constructor
	public Shingle(int docId, int hashcode) {
		super();
		this.docId = docId;
		this.hashcode = hashcode;
	}
	public Shingle() {
		super();
	}
	//Getters and Setters
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public int getHashcode() {
		return hashcode;
	}
	public void setHashcode(int hashcode) {
		this.hashcode = hashcode;
	}

}
