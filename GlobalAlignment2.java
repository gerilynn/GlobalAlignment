package HW2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GlobalAlignment2 {
	public static void main(String[] args) throws FileNotFoundException{
	
		int a = Integer.parseInt(args[0]);
		int b = Integer.parseInt(args[1]);
		int c = Integer.parseInt(args[2]);
		String seq1 = "";
		String seq2 = "";
		String input = args[3];
		File fasta = new File(input);
		Scanner scanner = new Scanner(fasta);
		while(scanner.hasNext() && (seq1 == "" || seq1.contains(">"))) {
			seq1 = scanner.nextLine();
		}
		while(scanner.hasNext() && (seq2 == "" || seq2.contains(">"))) {
			seq2 = scanner.nextLine();
		}	
		int[][] V = fillV(seq1, seq2, a, b, c);
		traceback(V, seq1, seq2, a, b, c);
	}
	// a match, b mismatch, c indel
	public static int[][] fillV(String seq1, String seq2, int a, int b, int c) {
		int n = seq1.length();
		int m = seq2.length();
		int[][] V = new int[n + 1][m + 1];
		for (int i = 0; i <= n; i++) {
			for (int j = 0; j <= m; j++) {
				int match = 0;
				if (i == 0) {
					V[i][j] = -j;
					System.out.print("-" + j + " ");
				} 
				else if (j == 0) {
					V[i][j] = -i;
					System.out.print("-" + i + " ");
				}
				else {//if not first row and column
					if (seq1.charAt(i-1) == seq2.charAt(j-1)) {
						match = a;
					} 
					else {
						match = b;
					}
					V[i][j] = maxOf3((V[i - 1][j - 1] + match), (V[i - 1][j] + c), (V[i][j - 1] + c));
					System.out.print(V[i][j] + " ");
				}
			}
			System.out.print("\n");
		}
		return V;
	}
	
	//this follows the path back to the origin, appending V[i][j]'s as we go, last first
	public static void traceback(int[][] V, String seq1, String seq2, int a, int b, int c) {
		int n = seq1.length();
		int m = seq2.length();
		char[] S1 = new char[2*(n+m)];//n+m definitely long enough
		char[] S2 = new char[2*(n+m)];
		int i = n;
		int j = m;
		int index = 0;
		while(i != 0 && j !=0){ //HERE's my problem. if i or j == 0 I need to step into a different calculation mode!
			if((V[i][j] - a == V[i-1][j-1]) && (seq1.charAt(i-1)==seq2.charAt(j-1))){//first see if the letters match!, then do the math to trace!
				S1[index]= seq1.charAt(i-1);
				S2[index] = seq2.charAt(j-1);
				j--;
				i--;
			}
			else if(V[i][j]-b == V[i-1][j-1]) {//(the two cases of diag arrow)
				S1[index]= seq1.charAt(i-1);
				S2[index] = seq2.charAt(j-1);
				j--;
				i--;
			}
			else if(V[i][j] -c == V[i-1][j]) {//(up arrow)
				S1[index] = seq1.charAt(i-1);
				S2[index] = '_';//i-1 because matrix and string indices are off by 1
				i--;
			}
			else if(V[i][j]-c == V[i][j-1]){//(left arrow)
				S1[index] = '_';
				S2[index] = seq2.charAt(j-1);
				j--;
			}
			else {
				System.out.println("Some of the possibilities evaded me!");
			}
			index++;
		}
		
		while(i==0 || j==0) {
			if(i==0) {
				S1[index] = '_';
				S2[index] = seq2.charAt(j-1);
				j--;
			}
			else if(j==0) {
				S1[index] = seq1.charAt(i-1);
				S2[index] = '_';
				i--;
			}	
			if (i==0 && j==0) {
				break;
			}
			index++;
		}
		System.out.println("optimal alignment:");
		System.out.print("seq1: ");
		//Oh yeah, I built my array from the bottom right, so I need to print it in reverse:
		for(int character = index; character >= 0; character--) {
			System.out.print(S1[character]);			
		}
		System.out.println();
		System.out.print("seq2: ");
		for(int character = index; character >= 0; character--) {
			System.out.print(S2[character]);			
		}
	}

	public static int maxOf3(int d, int u, int l) {
		if(d>=u && d>= l) {
			return d;
		}
		else if(u>=l) {
			return u;
		}
		else {
			return l;
		}
	}

}
