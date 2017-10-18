import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;
import java.util.Random;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;

public class group39 implements ContestSubmission
{
    Random rnd_;
    ContestEvaluation evaluation_;
    private int evaluationsLimit_;
    private Map<String, Boolean> evaluationType = new HashMap<String, Boolean>();
    
    public group39()
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
        // reading in the passed arguments
        // int popSize = Integer.parseInt(System.getProperty("populationSize"));
        // boolean speciation = Boolean.parseBoolean(System.getProperty("speciation"));
        // double maxPopDistance = Double.parseDouble(System.getProperty("maxPopDistance"));

        int popSize = 20;
        double maxPopDistance = 0.5;


        Population p = new Population(evaluationsLimit_, evaluation_, evaluationType, popSize, maxPopDistance);

        int i = 0;
        while(true){
            p.speciate();
            if(p.evaluate() == false){
                break;
            }       
            p.calculateNoOffspring();
            p.generateNextGen();
            i++;
        }
    }
}
