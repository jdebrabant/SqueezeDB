

import ilog.concert.*;
import ilog.cplex.*;

public class SumSolver 
{
	public static double[] sumSolver(int min, int max, double epsilon, boolean solveMax)
	{
		try 
		{
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
				coeffs[i] = (double)(i+1);
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
				double[] val = cplex.getValues(epsilonV);
				cplex.end();
				return val;
			} 
			else 
			{
				cplex.end();
				return null;
			}
		} 
		catch (IloException e) 
		{
			System.err.println("Concert exception '" + e + "' caught");
			return null;
		}
	}
	
	public static void main(String[] args) 
	{
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
	}
} 
