//
//  QueryBenchmarker.java
//  
//
//  Created by Justin on 12/7/11.
//  Copyright 2011 Florida State University. All rights reserved.
//

public class QueryBenchmarker 
{
	public static void main(String [] args)
	{
		BufferedReader exact_in, sampled_in; 
		BufferedWriter out; 
		
		String query; 
		
		Connection conn; 
		Statement stmt;
		ResultSet result; 
		
		int exact_sum; 
		int estimate_sum; 
		
		long start_time, end_time; 
		long total_time_sampled, total_time_exact; 
		
		double sum_ratio = 0; 
		
		Vector<String> exact_queries = new Vector<String>(); 
		Vector<String> sampled_queries = new Vector<String>(); 
		
		if(args.length != 5)
		{
			System.out.println("Usage: <user> <db> <exact query file> <sample query file> <result file>"); 
			System.exit(1); 
		}
				
		try 
		{
			conn = DBConnect(args[0], "", args[1]);
			stmt = conn.createStatement(); 
			
			exact_in = new BufferedReader(new FileReader(args[2])); 
			sampled_in = new BufferedReader(new FileReader(args[3])); 
			
			out = new BufferedWriter(new FileWriter(args[4])); 
			
			// read in queries from file
			while((query = exact_in.readLine()) != null)
			{
				exact_queries.add(query); 
			}
			
			// read sampled queries from file
			while((query = sampled_in.readLine()) != null)
			{
				sampled_queries.add(query); 
			}
			
			exact_in.close(); 
			sampled_in.close(); 
			
			out.write("exact sum,estimated sum,solution 1 min,solution 1 max,solution 2 min,solution 2 max,sum ratio\n"); 
			
			for(int i = 0; i < exact_queries.size(); i++)
			{
				query = exact_queries.get(i); 
				
				start_time = System.currentTimeMillis(); 
				result = stmt.executeQuery(query); 
				exact_sum = processResultSum(result, out); 
				start_time = System.currentTimeMillis(); 
				
				result.close(); 
				
				query = sampled_queries.get(i); 
				
				start_time = System.currentTimeMillis(); 
				result = stmt.executeQuery(query);
				estimate_sum = processResultSumSampled(result, out); 
				start_time = System.currentTimeMillis(); 
				
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
