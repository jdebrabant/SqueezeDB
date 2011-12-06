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
		
		int exact_sum; 
		int estimate_sum; 
		
		double sum_ratio = 0; 
		
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
			
			System.out.println("...executing " + queries.size() + " queries"); 
			
			out.write("exact sum,estimated sum,solution 1 min,solution 1 max,solution 2 min,solution 2 max,sum ratio\n"); 
			
			for(int i = 0; i < queries.size(); i++)
			{
				query = queries.get(i); 
				result = stmt.executeQuery(query); 
				exact_sum = processResultSum(result, out); 
				result.close(); 
				
				i++; 
				query = queries.get(i); 
				result = stmt.executeQuery(query); 
				
				estimate_sum = processResultSumSampled(result, out); 
				result.close(); 
				
				sum_ratio = Math.abs(exact_sum-estimate_sum)/(double)exact_sum; 
				System.out.print("sum ratio: " + sum_ratio + "\n\n"); 
				out.write(sum_ratio + "\n"); 

			}
			
			out.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
		
		
	}
	
	public static int processResultSum(ResultSet result, BufferedWriter result_out)
	{
		int sum = 0; 
		
		try
		{
			while(result.next())
			{
				sum += result.getInt("c_1"); 
			}
			
			System.out.println("sum: " + sum); 
			
			result_out.write(sum + ","); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
		
		return sum; 
	}
	
	
	public static int processResultSumSampled(ResultSet result, BufferedWriter result_out)
	{
		HashMap<Integer, Integer> frequencies = new HashMap<Integer, Integer>(); 
		
		double [] selectivity;  
		
		int sample_size = 15379; 
		int db_size = 1000000; 
		
		double eta = db_size/((double)sample_size); 
		
		double query_selectivity = 0; 
		
		CplexSolution solution1_min; 
		CplexSolution solution1_max; 
		
		CplexSolution solution2_min;
		CplexSolution solution2_max; 

		Integer k; 
		Integer v; 
		
		int min = 10000, max = 0; 
		
		int sum = 0; 
		
		try 
		{			
			while(result.next())
			{
				k = new Integer(result.getInt("c_1")); 
				
				// update max and min
				if(k.intValue() > max)
					max = k.intValue(); 
				else if(k.intValue() < min)
					min = k.intValue(); 
				
				
				if(frequencies.containsKey(k))  // already in the list
				{
					v = frequencies.get(k); 
					v += 1; 
					
					frequencies.put(k, new Integer(v)); 
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
				
				if(v != null)
				{
					selectivity[index] = v.intValue() / ((double)sample_size); 
				}
				else
				{
					selectivity[index] = 0; 
				}
			}
			
			for(int i = 0; i < (max-min+1); i++)
			{
				query_selectivity += selectivity[i]; 
			}
			
			for(int i = 0; i < (max-min+1); i++)
			{
				sum += selectivity[i] * (min+i) * db_size; 
			}
			
			/*
			for(int i = 0; i < (max-min+1); i++)
				System.out.println("selectivity " + i + " = " + selectivity[i] + "\n"); 
			
			System.out.println("eta = " + eta); 
			*/
			
			System.out.println("estimated:  " + sum);
			
			solution1_min = SumSolver.sumSolver(min, max, .01, false); 
			solution1_max = SumSolver.sumSolver(min, max, .01, true); 
			
			solution1_min.objective_value *= db_size; 
			solution1_max.objective_value *= db_size; 
			
			System.out.print("confidence interval 1: (" + 
					  solution1_min.objective_value + ", " + solution1_max.objective_value + ")\n"); 
						
			solution2_min = SumSolver2.sumSolver(min, max, query_selectivity, selectivity, eta, .02, false); 
			solution2_max = SumSolver2.sumSolver(min, max, query_selectivity, selectivity, eta, .02, true); 
			
			solution2_min.objective_value *= db_size; 
			solution2_max.objective_value *= db_size; 
			
			System.out.print("confidence interval 2: (" + 
							 solution2_min.objective_value + ", " + solution2_max.objective_value + ")\n");
			
			result_out.write(sum + "," + solution1_min.objective_value + "," + solution1_max.objective_value +
					  solution2_min.objective_value + "," + solution2_max.objective_value + ","); 
			
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out); 
			//System.out.println(e.getMessage()); 
		}
		
		return sum; 
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