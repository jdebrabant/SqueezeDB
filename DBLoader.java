/********************************************************************************************************
 - File: DBLoader.java
 - Author: Justin A. DeBrabant (debrabant@cs.brown.edu)
 - Usage: java DBLoader 
 - Description: 
	Prompts the user for a filename and the database information and will load the table contained in
	the file into the specified database. The primary key for each tuple is the line number. The format
	of the table in the file is one tuple per line with columns separated by spaces. 
 ********************************************************************************************************/ 

import java.util.*;
import java.sql.*;
import java.io.*;

public class DBLoader
{
	public static final int INSERT_BATCH_SIZE = 1000; 
	
	public static void main(String [] args)
	{
		Connection conn;
		Statement stmt;
		
		BufferedReader data_in;
		Scanner in; 
		
		// connection strings
		String db_url, user, password; 
		
		String table; 
		
		// query vars
		String query;
		
		// parsing vars
		String filename;
		String line;
		StringTokenizer tokenizer;
		
		int tuple_id; 

		in = new Scanner(System.in);

		System.out.print("Enter the data file: ");
		filename = in.next(); 
		
		db_url = "jdbc:postgresql://";
		System.out.print("Enter db server: ");
		db_url += in.next();
		db_url += "/";
		
		System.out.print("Enter user: ");
		user = in.next();
		
		System.out.print("Enter database: ");
		db_url += in.next();
		
		System.out.print("Enter table name: ");
		table = in.next(); 
		
		try   // establish database connection and open data file
		{
			conn = DriverManager.getConnection(db_url, user, "");
			stmt = conn.createStatement();
						
			data_in = new BufferedReader(new FileReader(filename)); // open input file
			
			tuple_id = 0; 
			while(((line = data_in.readLine()) != null))  // read tuples, line by line
			{
				query = "INSERT INTO " + table + " VALUES (" + tuple_id;
				
				tokenizer = new StringTokenizer(line, " "); 
				
				while(tokenizer.hasMoreTokens())  // parse all the values in this tuple
				{
					query += ", " + tokenizer.nextToken(); 
				}
				
				query += ");"; 
								
				stmt.executeUpdate(query); 
				
				tuple_id++; 
			}
			
		}
		catch (Exception e) 
		{
			System.out.println(e.getMessage());
		}
	}
}