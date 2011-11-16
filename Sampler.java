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
		
		StringTokenizer tokenizer; 
		
		if(args.length != 3)
		{
			System.out.println("usage: java Sampler <input file> <output file> <sample size>");
			System.exit(1); 
		}
		
		try 
		{
			in = new RandomAccessFile(new File(args[0]), "r"); 
			out = new BufferedWriter(new FileWriter(args[1]));
			
			sample_size = Integer.parseInt(args[2]); 
			
			rand = new Random(); 
			
			// determine the number of tuples in the table being sample from 
			table_size = 0; 
			while(((tuple = in.readLine()) != null))
			{
				table_size++; 
			}
			in.seek(0); // reset read pointer to start of file
			
			// randomly sample s tuples
			for(int i = 0; i < sample_size; i++)
			{
				random_tuple = rand.nextInt(table_size); 
				
				for(int j = 0; j < random_tuple; j++) // skip past first (random_tuple - 1) tuples in table
				{
					in.readLine(); 
				}
				
				tuple = in.readLine(); // read the randomly selected tuple
				
				out.write(tuple + "\n"); // write the tuple to the output file
				
				in.seek(0); // reset read pointer to start of file
			}

			out.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
	}
}