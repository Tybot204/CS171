package examples;

import cspSolver.BTSolver;
import cspSolver.BTSolver.ConsistencyCheck;
import cspSolver.BTSolver.ValueSelectionHeuristic;
import cspSolver.BTSolver.VariableSelectionHeuristic;
import sudoku.SudokuFile;
import sudoku.SudokuBoardReader;

public class monsterSudokuSolver {
	public static void main(String[]args){
		if(argsExist(args)){
			solveSudoku(args);
		}
		else{
			System.out.println("Invalid amount of arguments\nExiting Program");
		}
		
	}
	public static boolean argsExist(String[] args){
		if(args.length >= 1){
			return true;
		}
		return false;
	}
	public static void solveSudoku(String[] args){
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
		}

		else
		{
			System.out.println("Failed to find a solution");
		}
	}
}