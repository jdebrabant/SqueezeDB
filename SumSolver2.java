

import ilog.concert.*;
import ilog.cplex.*;

public class SumSolver2
{
	/**
	 * XXX The returned solution should be multiplied by the size of the
	 * original (large) database. Or we could pass it as a parameter here
	 * and do the multiplication in this method.
	 */
	public static CplexSolution sumSolver(int min, int max, double
			querySelectivity, double[] selectivities, double eta,
			double epsilon, boolean solveMax)
	{
		try 
		{
			CplexSolution solution = null;
			
			double [] solution_values; 
			double obj_value = 0; 
			
			IloCplex cplex = new IloCplex();
			
			double[] lowerBound = new double[max - min + 1];
			double[] upperBound = new double[max - min + 1];
			
			for(int i=0; i<max-min+1; i++)
			{
				lowerBound[i] = Math.max(selectivities[i] * (1 - eta) / eta, -epsilon);
				upperBound[i] = Math.min((1 - (querySelectivity - selectivities[i]) / eta) -
							selectivities[i], epsilon);
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
			cplex.addLe(sumExpr, Math.min(1-querySelectivity, epsilon));
			cplex.addGe(sumExpr, Math.max(querySelectivity * (1 - eta) / eta , -epsilon));
			
			cplex.setOut(null); 
			
			if(cplex.solve()) 
			{
				solution_values = cplex.getValues(epsilonV);
				obj_value = cplex.getObjValue(); 
										 
				solution = new CplexSolution(solution_values, obj_value); 
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
	
	// XXX Fix for the new optimization problem
	public static void main(String[] args) 
	{

	}
} 

