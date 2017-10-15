import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

import java.util.Random;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;



public class group39 implements ContestSubmission
{
    private static final int dimensions = 10;
    private static final boolean speciation = true;
	private Random rnd_;
    private ContestEvaluation evaluation_;
    private int evaluations_limit_;

    private Map<String, Boolean> evaluationType = new HashMap<>();

        

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
        evaluationType.put("Multimodal", isMultimodal);
        evaluationType.put("Regular", hasStructure);
        evaluationType.put("Separable", isSeparable);
    }
    
    public void test(){
        System.out.println("a");
    }

    public void run()
    {
        // File inputFile = new File("hparams.txt");
        // File tempFile = new File("tempParams.txt");

        // BufferedReader reader = null;
        // BufferedWriter writer = null;
        // String trimmedLine = "";

        // try {
        //     reader = new BufferedReader(new FileReader(inputFile));
        //     writer = new BufferedWriter(new FileWriter(tempFile));


        //     String currentLine;

        //     int i = 0;
        //     while((currentLine = reader.readLine()) != null) {
        //         // trim newline when comparing with lineToRemove
        //         trimmedLine = currentLine.trim();
        //         if(i==0) continue;
        //         writer.write(currentLine + System.getProperty("line.separator"));
        //         i++;
        //     }        

        //     writer.close(); 
        //     reader.close(); 
        // }
        // catch(IOException ex){
        //     ex.printStackTrace();
        // }
        // boolean successful = tempFile.renameTo(inputFile);

        // System.out.println(trimmedLine);


        Population population = new Population(evaluationType, evaluation_, evaluations_limit_, speciation, no_of_species);
        population.initSpecies();

        for(int i=0;i<population.getNoOfGenerations();i++){
            population.nextGeneration(); 
        }

    }
}
