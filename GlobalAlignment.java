package HW2;

//You can assume a simplified scoring function delta that has the following form:
//delta(match) = a
//delta(mismatch) = b
//delta(insertion/deletion) = c
//
//The a, b, and c values and filename should be supplied to your program via command-line parameters, 
//e.g. myAlign 2 -1 -1 test.fa (or something like this)

public class GlobalAlignment {
	public static String seq1 = "acccaxxx";
	public static String seq2 = "yyyyyaccca";
	
	//public static String seq1 = "MTEITAAMVKELRESTGAGMMDCKNALSETNGDFDKAVQLLREKGLGKAAKKADRLAAEGLVSVKVSDDFTIAAMRPSYLSYEDLDMTFVENEYKALVAELEKENEERRRLKDPNKPEHKIPQFASRKQLSDAILKEAEEKIKEELKAQGKPEKIWDNIIPGKMNSFIADNSQLDSKLTLMGQFYVMDDKKTVEQVIAEKEKEFGGKIKIVEFICFEVGEGLEKKTEDFAAEVAAQL"; // The first 30 chars from file
	//public static String seq2 = "SATVSEINSETDFVAKNDQFIALTKDTTAHIQSNSLQSVEELHSSTINGVKFEEYLKSQIATIGENLVVRRFATLKAGANGVVNGYIHTNGRVGVVIAAACDSAEVASKSRDLLRQICMH";
	public static int n = seq1.length();
	public static int m = seq2.length();
	public static int[][] V = new int[n + 1][m + 1];
	//Delete the following after I can read in a file.
	public static int a = 2;
	public static int b = -1;
	public static int c = -1;

	public static void main(String[] args) {
		fillV();
		traceback();
	}
	// a match, b mismatch, c indel
	public static void fillV() {
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
	}
	
	//this follows the path back to the origin, appending V[i][j]'s as we go, last first
	public static void traceback() {
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
		
		
		//AAA
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
