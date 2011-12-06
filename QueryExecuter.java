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
				
				processResult(result, out); 
			}
			
			out.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
		
		
	}
	
	public static void processResult(ResultSet result, BufferedWriter result_out)
	{
		HashMap<Integer, Integer> frequencies = new HashMap<Integer, Integer>(); 
		
		try 
		{
			Integer k; 
			Integer v; 
			
			int min = 10000, max = 0; 
			
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
				}
				else
				{
					frequencies.put(k, new Integer(1)); 
				}
			}
			
			result_out.write("(" + min + ", " + max + ")"); 
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