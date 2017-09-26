import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import org.vu.contest.ContestEvaluation;

public class Population
{
	private ArrayList<RealGenotype> population = new ArrayList<RealGenotype>(); 
	private int noOfSurvivors = 0;
	private String evaluationType = "";
	private double evaluationLimit = 0.0;
	ContestEvaluation evaluation_;

	//////// hyperparameters to be set according to the type of function
    int populationSize = 0;
    int noOfGenerations = 0;
    // standard deviation for the gaussian selecting the parents
	double selection_std = 0;


	public Population(String evType, ContestEvaluation evaluation, int evLimit){
		evaluation_ = evaluation;
		evaluationType = evType;
		evaluationLimit = evLimit;
		setHyperparameters();

		initPopulation();
	}


	private void setHyperparameters(){
		System.out.println(evaluationType);
		if(evaluationType.equals("Multimodal")){
			populationSize = 100;
			selection_std = 30;
			noOfGenerations = (int)(evaluationLimit/populationSize);
			noOfSurvivors = 20;
		}else if(evaluationType.equals("Regular")){
            populationSize = 1;
			selection_std = 1;
			noOfGenerations = (int)(evaluationLimit/populationSize);
			noOfSurvivors = 1;
        }else{
            populationSize = 50;
			selection_std = 10;
			noOfGenerations = (int)(evaluationLimit/populationSize);
			noOfSurvivors = 10;
        }
	}


	private void initPopulation(){
		for(int i=0;i<populationSize;i++){
        	population.add(new RealGenotype(-5,5));
        }
	}

	public void nextGeneration(){
		selection();
		// System.out.println(population.get(populationSize-1).getFitness());
		// System.out.println(Arrays.toString(sel));
		recombinationAndMutation();
	}

	private void selection(){
		for(int i=0;i<populationSize-noOfSurvivors;i++){
			population.get(i).setFitness((double) evaluation_.evaluate(population.get(i).getValue()));
		}
		// sort the population according to fitness
		// index 100 is the fittest member
		population.sort(Comparator.comparing(RealGenotype::getFitness));
	}

	private void recombinationAndMutation(){
		Random r = new Random();
		ArrayList<RealGenotype> new_population = new ArrayList<>();
		int mom_idx = 0;
		int dad_idx = 0;
		for(int i=0;i<populationSize - noOfSurvivors;i++){
			do{
				mom_idx = (populationSize-1) - (int) Math.abs(r.nextGaussian()*selection_std);
			} while (mom_idx < 0);
			do{
				dad_idx = (populationSize-1) - (int) Math.abs(r.nextGaussian()*selection_std);
			} while (mom_idx == dad_idx || dad_idx < 0);

			RealGenotype child = RealGenotype.breed2(population.get(mom_idx), population.get(dad_idx));
			
			// add mutation
			child.mutate(0.5);

			new_population.add(child);
		}
		// add the parents to the next generation
		for(int i=populationSize-noOfSurvivors;i<populationSize;i++){
			new_population.add(population.get(i));
		}
		population = new_population;
	}

    public ArrayList<RealGenotype> getPopulation(){
    	return population;
    }

    public int getNoOfGenerations(){
    	return noOfGenerations;
    }
}