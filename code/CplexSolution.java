//
//  CplexSolution.java
//  
//
//  Created by Justin on 12/6/11.
//  Copyright 2011 Florida State University. All rights reserved.
//

public class CplexSolution 
{
	public double [] solution; 
	public double objective_value; 
	
	public CplexSolution(double [] s, double v)
	{
		solution = new double[s.length]; 
		
		for(int i = 0; i < s.length; i++)
			solution[i] = s[i]; 
		
		objective_value = v; 
	}
	
}
