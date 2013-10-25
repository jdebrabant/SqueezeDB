/********************************************************************************************************
 - File: CplexTester.java
 - Author: Justin A. DeBrabant (debrabant@cs.brown.edu)
 - Usage: java CplexTester <[max, min]> <output file> <domain min> <domain max> 
 - Description:
	
 ********************************************************************************************************/ 

import java.util.*; 
import java.io.*; 

public class CplexTester 
{
	public static final double EPSILON = .02; 
	
	public static void main(String [] args)
	{
		BufferedWriter out; 
		int min, max; 
		
		if(args.length != 4 || (!args[0].equals("max") && !args[0].equals("min"))) 
		{
			System.out.println("usage: java CplexTester <[max, min]> <output file> <domain min> <domain max>"); 
			System.exit(1); 
		}
		
		try
		{
			out = new BufferedWriter(new FileWriter(args[1])); 
			
			min = Integer.parseInt(args[2]); 
			max = Integer.parseInt(args[3]); 
			
			if(args[0].equals("max"))
			{
				out.write("Maximize" + "\n" + "  obj: "); 
			}
			else
			{
				out.write("Minimize" + "\n" + "  obj: "); 
			}
			
			// write objective function 
			out.write(min + " x" + min); 
			for(int i = min+1; i <= max; i++)
			{
				out.write(" + " + i + " x" + i); 
			}
			out.write("\nSubject To \n");
			
			out.write("c1: " + "x" + min); 
			for(int i = min+1; i <= max; i++)
			{
				out.write(" + " + "x" + i); 
			}
			/**
			 * This and following commented out lines can be used to
			 * write the linear program in "standard" form. 
			 * Notice that there isn't such a commented line for the
			 * objective function, because apparently CPLEX does not
			 * allow to specify constants in the objective function.
			 * This means that, when using the standard form, one has
			 * to *subtract* (EPSILON * \sum_{i \in [m,M]} i) from the
			 * solution given by CPLEX in order to obtain the true
			 * value of the optimized objective function. Matteo
			 */
			//out.write(" <= " + ((max - min + 2) * EPSILON) + "\n"); 
			out.write(" <= " + EPSILON + "\n"); 

			out.write("c2: " + "x" + min);
			for (int i = min+1; i <= max; i++)
			{
			  	out.write(" + " + "x" + i);
			}
			//out.write(" >= " + ((max - min) * EPSILON) + "\n"); 
			out.write(" >= -" + EPSILON + "\n"); 
			
			out.write("Bounds\n"); 
			for(int i = min; i <= max; i++)
			{
				//out.write("0 <= x" + i + " <= " + (2 * EPSILON) + "\n"); 
				out.write("-" + EPSILON + " <= x" + i + " <= " +  EPSILON + "\n"); 
			}
			out.write("END"); 
			
			out.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage()); 
		}
	}
}






