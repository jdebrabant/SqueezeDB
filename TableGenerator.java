/********************************************************************************************************
 - File: TableGenerator.java
 - Author: Justin A. DeBrabant (debrabant@cs.brown.edu)
 - Usage: java TableGenerator <output file> <number of tuples> <number of columns> <max attribute range>
 - Description: 
	Randomly generates a relation with t tuples each with c columns. Column values are randomly chosen 
	in the range [0, m). Output is printed to the specified file in a space-delimited t x c matrix. 
 ********************************************************************************************************/ 

import java.util.*; 
import java.io.*; 

public class TableGenerator
{
	public static void main(String [] args)
	{
		BufferedWriter out; 
		String tuple; 
		Random rand; 
		int num_tuples, num_columns, max_range; 
		
		if(args.length != 4)
		{
			System.out.println("usage: java TableGenerator <output file> <number of tuples> <number of columns> <max attribute range>");
			System.exit(1); 
		}

		try 
		{
			out = new BufferedWriter(new FileWriter(args[0]));
			rand = new Random(); 
			
			num_tuples = Integer.parseInt(args[1]); 
			num_columns = Integer.parseInt(args[2]); 
			max_range = Integer.parseInt(args[3]); 
			
			for(int i = 0; i < num_tuples; i++)
			{
				tuple = ""; 
				
				for(int j = 0; j < num_columns; j++)
				{
					tuple += rand.nextInt(max_range + 1);  
					
					if(j != (num_columns-1))
						tuple += " "; 
				}
				tuple += "\n"; 
				
				out.write(tuple); 
			}
			
			out.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
	}
}