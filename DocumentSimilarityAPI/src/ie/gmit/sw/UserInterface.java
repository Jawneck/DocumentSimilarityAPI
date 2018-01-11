//A UserInterface class which provides the command line menu.
//Author: Danielis Joniskis

package ie.gmit.sw;

import java.util.Scanner;

public class UserInterface {

	public void show() throws InterruptedException {	
		String fileA;
		String fileB;
		int shingles;
		
		//Opening the Scanner
		Scanner scanner = new Scanner(System.in);
		
		//Prompting the user to enter file A path
		System.out.print("\nPlease enter file A: ");
		fileA = scanner.nextLine();
		
		//Prompting the user to enter file B path
		System.out.print("\nPlease enter file B: ");
		fileB = scanner.nextLine();
		
		//Prompting the user to enter the number of shingles
		System.out.print("\nPlease enter the number of shingles: ");
		shingles = scanner.nextInt();
		
		new Launcher(fileA,fileB,100,shingles);
		//Closing the Scanner
		scanner.close();
	}

}