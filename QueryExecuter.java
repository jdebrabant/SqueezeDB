/***************************************************************************************************
 * File: QueryExecuter.java
 * Authors: Justin A. DeBrabant (debrabant@cs.brown.edu)
 * Description: 
 ***************************************************************************************************/ 


import java.util.*; 
import java.lang.*; 
import java.io.*; 
import java.sql.*; 

public class QueryExecuter
{
	
	public static void main(String [] args)
	{
		BufferedReader in; 
		BufferedWriter out; 

		String query; 
		
		Connection conn; 
		Statement stmt;
		ResultSet result; 
		
		Vector<String> queries = new Vector<String>(); 
		
		if(args.length != 4)
		{
			System.out.println("Usage: <user> <db> <query file> <result file>"); 
			System.exit(1); 
		}
		
		try 
		{
			conn = DBConnect(args[0], "", args[1]);
			stmt = conn.createStatement(); 
			
			in = new BufferedReader(new FileReader(args[2])); 
			out = new BufferedWriter(new FileWriter(args[3])); 
			
			// read in queries from file
			while((query = in.readLine()) != null)
			{
				queries.add(query); 
			}
			
			for(int i = 0; i < queries.size(); i++)
			{
				result = stmt.executeQuery(queries.get(i)); 
				processResultSum(result, out); 
				result.close(); 
				
				i++; 
				result = stmt.executeQuery(queries.get(i)); 
				processResultSumSampled(result, out); 
				result.close(); 
			}
			
			out.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
		
		
	}
	
	public static void processResultSum(ResultSet result, BufferedWriter result_out)
	{
		int sum = 0; 
		
		try
		{
			while(result.next())
			{
				sum += result.getInt("c_1"); 
			}
			
			result_out.write("Actual Sum: " + sum + "\n"); 
		}
		catch(Exception e)
		{
		}
	}
	
	
	public static void processResultSumSampled(ResultSet result, BufferedWriter result_out)
	{
		HashMap<Integer, Integer> frequencies = new HashMap<Integer, Integer>(); 
		
		double [] selectivity;  
		
		int sample_size = 15379; 
		int db_size = 1000000; 
		
		double eta = ((double)sample_size)/db_size; 
		
		double query_selectivity = 0; 
		
		CplexSolution solution1_min; 
		CplexSolution solution1_max; 
		
		CplexSolution solution2_min;
		CplexSolution solution2_max; 

		
		try 
		{
			Integer k; 
			Integer v; 
			
			int min = 10000, max = 0; 
			
			int sum = 0; 
			
			while(result.next())
			{
				k = new Integer(result.getInt("c_1")); 
				sum += k.intValue(); 
				
				// update max and min
				if(k.intValue() > max)
					max = k.intValue(); 
				else if(k.intValue() < min)
					min = k.intValue(); 
				
				
				if(frequencies.containsKey(k))  // already in the list
				{
					v = frequencies.get(k); 
					v += 1; 
				}
				else
				{
					frequencies.put(k, new Integer(1)); 
				}
			}
			
			selectivity = new double[max-min+1]; 
			
			for(int i = min, index = 0; i <= max; i++, index++)
			{	
				v = frequencies.get(new Integer(i)); 
				
				selectivity[index] = v.intValue() / ((double)sample_size); 
			}
			
			for(int i = 0; i < (max-min+1); i++)
			{
				query_selectivity += selectivity[i]; 
			}
			
			result_out.write("Sampled Sum:  " + sum + "\n");
			
			solution1_min = SumSolver.sumSolver(min, max, .01, false); 
			solution1_max = SumSolver.sumSolver(min, max, .01, true); 
			
			result_out.write("\t SumSolver1 confidence interval (" + 
					  solution1_min.objective_value + ", " + solution1_max.objective_value + ")\n"); 
			
			solution2_min = SumSolve2.sumSolver(min, max, query_selectivity, selectivity, eta, .02, false); 
			solution2_max = SumSolve2.sumSolver(min, max, query_selectivity, selectivity, eta, .02, true); 
			
			result_out.write("\t SumSolver2 confidence interval (" + 
							 solution2_min.objective_value + ", " + solution2_max.objective_value + ")\n");
			
			
		}
		catch(Exception e)
		{
		}
	}
	
	
	public static Connection DBConnect(String user, String password, String db)
	{
		Connection c = null; 

		String url = new String("jdbc:postgresql:");
		
		url += db; 
		
		try 
		{
			Class.forName("org.postgresql.Driver");  // load the driver
			c = DriverManager.getConnection(url, user, password);
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
		
		return c; 
	}

}