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

	private int pop_count = 100;
	private ArrayList<double[]> population = new ArrayList<double[]>(); 

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

    private double[] createMember(){
    	double[] member = new double[dimensions];
    	for(int i = 0; i < dimensions; i++) {
    		member[i] = rnd_.nextDouble();
		}
		return member;
    }


    private double convexScore(){
    	double score = 0.0;
    	for(int i=0;i<pop_count-1;i++){
    		double[] member1 = population.get(i);
    		double[] member2 = population.get(i+1);
    		double fitness1 = (double) evaluation_.evaluate(member1);
    		double fitness2 = (double) evaluation_.evaluate(member2);

    		// score is high when close members have a large difference in fitness
    		score += Math.abs(fitness1-fitness2) / distance(member1,member2);
    	}
    	System.out.println("convexscore = " + score);
    	return score;
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
        for(int i=0;i<pop_count;i++){
        	population.add(createMember());
        }
        convexScore();

        // calculate fitness
        while(evals<evaluations_limit_-1000){
            // Select parents
            // Apply crossover / mutation operators
            // double child[] = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
            // Check fitness of unknown fuction
            Double fitness = (double) evaluation_.evaluate(population.get(0));

            evals++;
            // Select survivors
        }
	}
}
