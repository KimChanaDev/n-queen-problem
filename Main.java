/*
	DataStructure Project1 - N-Queen problem
	Created by:
		6313124 Chanawee Sateinteeraphap
		6313125 Chayakorn Jullanee
		6313132 Piyawat Wirotkitphaiboon
		6313219 Sorawit Kuhakasamsin
*/

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.Predicate;

class CustomException extends Exception {
	public CustomException() {
		super();
	}
}

class NQueensProblem {
	private final int N; //NxN board dimension
	private int startX = 0, startY = 0; //initial queen place position
	private int[] board; /* Create board of size NxN using N-length array,
		 where array index = row(y), array value = column(x) */
	private int foundSolution = 0;

	public NQueensProblem(int n, boolean showEmptyOnly) {
		N = n;
		createBoard();
		if (showEmptyOnly) { display(); return; }
		placeQueen(0); //start an attempt to place queens
		if ( foundSolution < 1 ) System.out.println("No solution!");
	}
	public NQueensProblem(int n, int sx, int sy) {
		N = n; startX = sx; startY = sy;
		createBoard();
		board[startY] = startX; //place first queen
		System.out.printf("Input: (x=%d, y=%d)\n", startX+1, startY+1);
		display();
		placeQueen(1); //start an attempt to place remaining queens after a queen is placed
		if ( foundSolution < 1 ) System.out.println("No solution!");
	}
	public NQueensProblem(int n) { this(n, false); }

	private void createBoard() {
		board = new int[N];
		for (int i = 0 ; i < N; i++) board[i] = -1;
	}
	private void placeQueen(int placed) {
		if (placed == N) { //N queens are placed successfully
			System.out.printf("Solution %d:\n", foundSolution+1);
			display();
			foundSolution++;
		} else {
			for (int i = 0; i < N; i++) {
				int x = (startX + i)%N;
				int y = (startY + placed)%N;
				if ( isSafe(x,y) ) {
					board[y] = x; //place queen on this row
					placeQueen(placed+1); //move to the next row
					board[y] = -1; // remove placed queen from this row
				}
			}
		}
	}
	private boolean isSafe(int x, int y) {
		for (int i = 0; i < N; i++) {
			if (board[i] == -1) continue;
			// placed_x = board[i], placed_y = i
			// position (x,y) is in the same row or column with placed queen
			if (board[i] == x || i == y) return false;
			// position (x,y) is on the same diagonal line with placed queen
			if (Math.abs(board[i] - x) == Math.abs(i - y)) return false;
		}
		return true;
	}
	private void display() {
		System.out.print("   ");
		for (int i=0; i<N; i++)
			System.out.printf(" %2d",i+1); //Display column index

		for (int i=0; i<N; i++) {
			System.out.printf("\n %2d ",i+1); //Display row index
			for (int j=0; j<N; j++) {
				//Display each position
				if (board[i] == j) System.out.print(" Q ");
				else System.out.print(" . ");
			}
		}
		System.out.println("\n");
	}
}

public class Main {
	private static final Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("---( N-queen problem solutions )---");
		do {
			int N = inputInt("Enter N (at least 4) = ", a -> (a < 4),
					"Entered number cannot be less than 4! Try again");
			int[] Nref = {N};
			new NQueensProblem(N, true); //display layout

			if (inputYN("Place first queen manually?")) {
				int sx = inputInt(String.format("Enter column of first queen (1-%d) = ", N), a -> (a < 1 || a > Nref[0]),
						"Entered column is out of range! Try again");
				int sy = inputInt(String.format("Enter row of first queen (1-%d) = ", N), a -> (a < 1 || a > Nref[0]),
						"Entered column is out of range! Try again");
				new NQueensProblem(N, sx-1, sy-1);
			} else new NQueensProblem(N); //get all solutions

		} while(inputYN("Continue?"));
		System.out.println("-----------END-----------");
		input.close();
	}
	private static int inputInt(String message) { return inputInt(message, x->true, ""); }
	private static int inputInt(String message, Predicate<Integer> customExceptionCondition, String customMessage) {
		int result;
		while (true) {
			try {
				System.out.print(message);
				result = input.nextInt();
				if (customExceptionCondition.test(result)) throw new CustomException();
				input.nextLine();
				return result;
			} catch(CustomException e) {
				System.out.println(customMessage);
			} catch(InputMismatchException e) {
				System.out.println("Entered value is not integer! Try again.");
			} catch(Exception e) {
				System.out.println("Something went wrong! Try again.");
			}
			input.nextLine();
		}
	}
	private static boolean inputYN(String message) {
		String value;
		String[] expectedYes = {"y","yes"};
		String[] expectedNo = {"n","no"};
		while (true) {
			try {
				System.out.print(message + " (y/n) ");
				value = input.nextLine();
				for (String s: expectedYes) if (value.equalsIgnoreCase(s)) return true;
				for (String s: expectedNo) if (value.equalsIgnoreCase(s)) return false;
				System.out.println("Invalid option. Please select y or n.");
			} catch(Exception e) {
				System.out.println("Something went wrong! Try again."); input.nextLine();
			}
		}
	}
}
