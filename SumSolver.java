

import ilog.concert.*;
import ilog.cplex.*;

public class SumSolver 
{
	/**
	 * XXX The returned solution should be multiplied by the size of the
	 * original (large) database. Or we could pass it as a parameter here
	 * and do the multiplication in this method.
	 */
	public static CplexSolution sumSolver(int min, int max, double epsilon, boolean solveMax)
	{
		try 
		{
			CplexSolution solution = null; 
			
			double obj_value = 0; 
			
			IloCplex cplex = new IloCplex();
			
			double[] lowerBound = new double[max - min + 1];
			double[] upperBound = new double[max - min + 1];
			
			for(int i=0; i<max-min+1; i++)
			{
				lowerBound[i] = -epsilon;
				upperBound[i] =  epsilon;
			}
			
			IloNumVar[] epsilonV  = cplex.numVarArray(max-min+1, lowerBound, upperBound);
			double[] coeffs = new double[max - min + 1];
			for(int i=0; i<max-min+1; i++)
			{
				coeffs[i] = (double)(min+i);
			}
			
			if(solveMax)
			{
				cplex.addMaximize(cplex.scalProd(epsilonV, coeffs));
			} 
			else 
			{
				cplex.addMinimize(cplex.scalProd(epsilonV, coeffs));
			}
			
			IloNumExpr[] ieExpr = new IloNumExpr[max - min + 1];
			for(int i=0; i<max-min+1; i++)
			{
				ieExpr[i] = cplex.prod(1.0, epsilonV[i]);
			}
			
			IloNumExpr sumExpr = cplex.sum(ieExpr);
			cplex.addLe(sumExpr, epsilon);
			cplex.addGe(sumExpr, -epsilon);
			
			if(cplex.solve()) 
			{
				solution = cplex.getValues(epsilonV);
				obj_value = cplex.getObjValue(); 
				
				solution = new CplexSolution(solution, obj_value); 
				
				cplex.end();
				
			} 
			else 
			{
				cplex.end();
			}
			
			return solution; 
		} 
		catch (IloException e) 
		{
			System.err.println("Concert exception '" + e + "' caught");
			return null;
		}
	}
	
	public static void main(String[] args) 
	{
		/*
		// Max
		double[] val1 = sumSolver(1, 100, 0.02, true);
		for (int j = 0; j < val1.length; j++)
		{
			System.out.println("EpsilonV" + (j + 1) + " = " + val1[j]);
		}
		
		// Min
		double[] val2 = sumSolver(1, 100, 0.02, false);
		for (int j = 0; j < val2.length; j++)
		{
			System.out.println("EpsilonV" + (j + 1) + " = " + val2[j]);
		}
		*/
	}
} 
