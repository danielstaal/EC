import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Random;


import org.vu.contest.ContestSubmission;
import org.vu.contest.ContestEvaluation;

public class Population
{
	private int popCount;
	private ArrayList<RealGenotype> population = new ArrayList<RealGenotype>(); 
	private int noOfSurvivors = 20;
	ContestEvaluation evaluation_;


	public Population(int popcount, ContestEvaluation evaluation){
		popCount = popcount;
		initPopulation();
		evaluation_ = evaluation;
	}

	private void initPopulation(){
		for(int i=0;i<popCount;i++){
        	population.add(new RealGenotype(-5,5));
        }
	}

	public void nextGeneration(){
		selection();
		System.out.println(population.get(99).getFitness());
		// System.out.println(Arrays.toString(sel));
		recombinationAndMutation();
	}

	private void selection(){
		for(int i=0;i<popCount-noOfSurvivors;i++){
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
		for(int i=0;i<popCount - noOfSurvivors;i++){
			do{
				mom_idx = 99 - (int) Math.abs(r.nextGaussian()*30);
			} while (mom_idx < 0);
			do{
				dad_idx = 99 - (int) Math.abs(r.nextGaussian()*30);
			} while (mom_idx == dad_idx || dad_idx < 0);

			RealGenotype child = RealGenotype.breed2(population.get(mom_idx), population.get(dad_idx));
			
			// add mutation
			child.mutate(0.5);

			new_population.add(child);
		}
		// add the parents to the next generation
		for(int i=popCount-noOfSurvivors;i<popCount;i++){
			new_population.add(population.get(i));
		}
		population = new_population;
	}

    public ArrayList<RealGenotype> getPopulation(){
    	return population;
    }
}