import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.ArrayList;
import java.lang.Math;

public class group39 implements ContestSubmission
{
	Random rnd_;
	ContestEvaluation evaluation_;

	private static final int dimensions = 10;

    private int evaluations_limit_;
	
	public group39()
	{
		rnd_ = new Random();
	}
	
	public void setSeed(long seed)
	{
		// Set seed of algorithms random process
		rnd_.setSeed(seed);
	}

	public void setEvaluation(ContestEvaluation evaluation)
	{
		// Set evaluation problem used in the run
		evaluation_ = evaluation;
		
		// Get evaluation properties
		Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluations_limit_ = Integer.parseInt(props.getProperty("Evaluations"));
		// Property keys depend on specific evaluation
		// E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

		// Do sth with property values, e.g. specify relevant settings of your algorithm
        if(isMultimodal){
            // Do sth
        }else{
            // Do sth else
        }
    }

    private double distance(double[] coor1, double[] coor2){
    	double distance = 0.0;
    	for(int i=0;i<dimensions;i++){
    		distance += Math.pow((coor1[i] - coor2[i]),2);
    	}
    	return Math.sqrt(distance);
    }
    
	public void run()
	{
		// Run your algorithm here
        
        int evals = 0;
        // System.out.println(evaluations_limit_);
        // init population

        // calculate fitness
        while(evals<evaluations_limit_-1000){
            // Select parents
            // Apply crossover / mutation operators
            // double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            // Check fitness of unknown fuction
            //Double fitness = (double) evaluation_.evaluate(population.get(0));

            evals++;
            // Select survivors
        }
	}
}
