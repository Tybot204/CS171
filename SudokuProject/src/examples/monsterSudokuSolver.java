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
	private static String inputFile;
	private static String outputFile;
	private static int solverTimeout;
	private static long preprocessingStartTime;
	private static long preprocessingEndTime;
	
	public static void main(String[]args){
		preprocessingStartTime = System.currentTimeMillis();
		if(parseInputs(args)){
			solveSudoku();
		}
		else{
			System.out.println("Invalid amount of arguments\nExiting Program");
		}
		
	}

	public static boolean parseInputs(String[] args){
		if(args.length >= 3){
			inputFile = args[0];
			outputFile = args[1];
			try {
				solverTimeout = Integer.parseInt(args[2]);
			} catch(NumberFormatException e) {
				return false;
			}
			return true;
		}
		return false;
	}

	private static void solveSudoku(){
		SudokuFile sf = SudokuBoardReader.readFile(inputFile);
		BTSolver solver = new BTSolver(sf);
		
		solver.setConsistencyChecks(ConsistencyCheck.None);
		solver.setValueSelectionHeuristic(ValueSelectionHeuristic.None);
		solver.setVariableSelectionHeuristic(VariableSelectionHeuristic.None);
		
		preprocessingEndTime = System.currentTimeMillis();
		Thread t1 = new Thread(solver);
		try
		{
			t1.start();
			t1.join(solverTimeout * 1000);
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
		}
		else
		{
			System.out.println("Failed to find a solution");
		}

		writeContentToFile(outputFile, solver.getSolverStats(preprocessingStartTime, preprocessingEndTime));
		System.out.println("Solution and log printed to file: " + outputFile);
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