/********************************************************************************************************
 - File: Sampler.java
 - Author: Justin A. DeBrabant (debrabant@cs.brown.edu)
 - Usage: java Sampler <input file> <output file> <sample size>
 - Description: 
	Creates a uniform random sample of the table contained in the input file. Randomly samples s tuples
	(with replacement) and outputs to the file specified with 1 tuple per row. 
 ********************************************************************************************************/ 

import java.util.*; 
import java.io.*; 

public class Sampler
{
	public static void main(String [] args)
	{
		
		RandomAccessFile in; 
		BufferedWriter out; 
		
		String tuple; 
		Random rand; 
		
		int table_size; 
		int sample_size; 
		int random_tuple; 
		int db_size; 
		
		StringTokenizer tokenizer;
		
		int [] rows_sampled; 
		
		if(args.length != 4)
		{
			System.out.println("usage: java Sampler <input file> <output file> <db size> <sample size>");
			System.exit(1); 
		}
		
		try 
		{
			in = new RandomAccessFile(new File(args[0]), "r"); 
			out = new BufferedWriter(new FileWriter(args[1]));
			
			db_size = Integer.parseInt(args[2]); 
			sample_size = Integer.parseInt(args[3]); 
			
			rows_sampled = new int[db_size]; 
			
			for(int i = 0; i < db_size; i++)
				rows_sampled[i] = 0; 
			
			rand = new Random(); 
			
			// randomly sample s tuples
			for(int i = 0; i < sample_size; i++)
			{
				random_tuple = rand.nextInt(db_size); 
				
				rows_sampled[random_tuple]++; 
			}
			
			for(int i = 0; i < db_size; i++)
			{
				tuple = in.readLine();
				
				if(tuple == null)
					break; 
				
				for(int j = 0; j < rows_sampled[i]; j++)
					out.write(tuple + "\n"); 
			}

			out.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
	}
}