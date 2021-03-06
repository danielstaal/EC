import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.lang.Math;

public class player39 implements ContestSubmission
{
    Random rnd_;
    ContestEvaluation evaluation_;
    private int evaluationsLimit_;
    private Map<String, Boolean> evaluationType = new HashMap<String, Boolean>();
    
    public player39()
    {
        rnd_ = new Random();
    }
        
    public void setSeed(long seed)
    {
        // Set seed of algortihms random process
        rnd_.setSeed(seed);
    }

    public void setEvaluation(ContestEvaluation evaluation)
    {
        // Set evaluation problem used in the run
        evaluation_ = evaluation;
                
        // Get evaluation properties
        Properties props = evaluation.getProperties();
        // Get evaluation limit
        evaluationsLimit_ = Integer.parseInt(props.getProperty("Evaluations"));
        // Property keys depend on specific evaluation
        // E.g. double param = Double.parseDouble(props.getProperty("property_name"));
        boolean isMultimodal = Boolean.parseBoolean(props.getProperty("Multimodal"));
        boolean hasStructure = Boolean.parseBoolean(props.getProperty("Regular"));
        boolean isSeparable = Boolean.parseBoolean(props.getProperty("Separable"));

        evaluationType.put("Multimodal", isMultimodal);
        evaluationType.put("Regular", hasStructure);
        evaluationType.put("Separable", isSeparable);      
    }
    
    public void run(){
        int popSize;
        double maxPopDistance;
        double mP;
        double mStdStart;
        double mStdEnd;
        boolean fitnessSharing;
        boolean speciation;
        if(evaluationType.get("Multimodal") == true)
        { // Schaffers or Katsuura
            if(evaluationType.get("Regular") == true)
            { // Schaffers
                popSize = 20;
                maxPopDistance = 0.5;
                mP = 0.2;
                mStdStart = 0.75;
                mStdEnd = 0.0001;
                fitnessSharing = false;
                speciation = false;
            }
            else
            { // Katsuura
                popSize = 100;
                maxPopDistance = 0.5;
                mP = 0.4;
                mStdStart = 0.03;
                mStdEnd = 0.00001;
                fitnessSharing = true;
                speciation = true;
            }
        }
        else // BentCigar
        {
            popSize = 13;
            maxPopDistance = 0;
            mP = 0.3;
            mStdStart = 0.25;
            mStdEnd = 0.0001;
            fitnessSharing = false;
            speciation = false;
        }
            /*
            int popSize            = Integer.parseInt(System.getProperty("populationSize"));
            double maxPopDistance  = Double.parseDouble(System.getProperty("maxPopDistance"));
            double mP              = Double.parseDouble(System.getProperty("mP"));
            double mStdStart       = Double.parseDouble(System.getProperty("mStdStart"));
            double mStdEnd         = Double.parseDouble(System.getProperty("mStdEnd"));
            boolean fitnessSharing = Boolean.parseBoolean(System.getProperty("fitnessSharing"));
            boolean speciation     = Boolean.parseBoolean(System.getProperty("speciation"));*/
        Population p = new Population(evaluationsLimit_, evaluation_, evaluationType,popSize
                            ,maxPopDistance, mP, mStdStart, mStdEnd, fitnessSharing, speciation);

        int i           = 0;
        int initialSize = p.populationSize_;
        while(true){
            p.speciate();
            if(p.evaluate() == false){
                break;
            }
            p.populationSize_ = (int) Math.round((double) initialSize * p.evaluations / evaluationsLimit_) + 1;
            p.calculateNoOffspring();
            p.generateNextGen();
            i++;
        }
    }
}
