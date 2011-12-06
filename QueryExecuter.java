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
				
				//processResult(result, out); 
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
		
		
	}
	
	public static void processResult(ResultSet result, BufferedWriter result_out)
	{
		try 
		{
			while(result.next())
			{
				
			}
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