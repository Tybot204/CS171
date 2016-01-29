package examples;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cspSolver.BTSolver;
import cspSolver.BTSolver.ConsistencyCheck;
import cspSolver.BTSolver.ValueSelectionHeuristic;
import cspSolver.BTSolver.VariableSelectionHeuristic;
import sudoku.SudokuFile;
import sudoku.SudokuBoardReader;

public class monsterSudokuSolver {
	private static long startTime;
	
	public static void main(String[]args){
		startTime = System.currentTimeMillis();
		if(argsExist(args)){
			solveSudoku(args);
		}
		else{
			System.out.println("Invalid amount of arguments\nExiting Program");
		}
		
	}
	public static boolean argsExist(String[] args){
		if(args.length >= 2){
			return true;
		}
		return false;
	}
	private static void solveSudoku(String[] args){
		SudokuFile sf = SudokuBoardReader.readFile(args[0]);
		BTSolver solver = new BTSolver(sf);
		
		solver.setConsistencyChecks(ConsistencyCheck.None);
		solver.setValueSelectionHeuristic(ValueSelectionHeuristic.None);
		solver.setVariableSelectionHeuristic(VariableSelectionHeuristic.None);
		
		Thread t1 = new Thread(solver);
		try
		{
			t1.start();
			t1.join(60000);
			if(t1.isAlive())
			{
				t1.interrupt();
			}
		}catch(InterruptedException e)
		{
		}


		if(solver.hasSolution())
		{
			System.out.println(solver.getSolution());
			writeContentToFile(args[1], solver.getSolverStats(startTime, System.currentTimeMillis()));
			System.out.println("Solution and log printed to file: " + args[1]);
		}

		else
		{
			System.out.println("Failed to find a solution");
		}
	}
	private static void writeContentToFile(String outputFile, String content){
		 File outFile = new File(outputFile);
		 if (!outFile.exists()) {
			 try {
			    // Create the new file with default permissions, etc.
			    outFile.createNewFile();
			} catch (IOException x) {
			    // Some other sort of failure, such as permissions.
			    System.err.format("createNewFile error: %s%n", x);
			}
		 }
		 try{
			 FileWriter fw = new FileWriter(outFile);
			 BufferedWriter bw = new BufferedWriter(fw);
			 bw.write(content);
			 bw.close();
		 } catch (IOException x){
			 System.err.format("Buffered writer error: %s%n", x);
		 }
		
	}
}