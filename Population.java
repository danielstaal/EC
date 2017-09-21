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
        	population.add(new RealGenotype());
        }
	}

	public void nextGeneration(){
		// Selection
		selection();
		System.out.println(population.get(99).getFitness());
		// System.out.println(Arrays.toString(sel));
		// recombination, mutation
		recombinationAndMutation();
		


	}

	private void selection(){
		// double[] fitnesses = new double[popCount];
		for(int i=0;i<popCount-noOfSurvivors;i++){
			// System.out.println(population.size());
			// System.out.println(i);
			// population.get(i).getValue();
			population.get(i).setFitness((double) evaluation_.evaluate(population.get(i).getValue()));
		}
		// // maybe this should be a deep copy
		// double[] temp = fitnesses.clone();
		// Arrays.sort(fitnesses);
		// int[] fittestMembers = new int[noOfSurvivors];

		// // find original index
		// // TODO: really badly written code with too many for loops, change this
		// for(int i=0;i<noOfSurvivors;i++){
		// 	for(int j=0;j<fitnesses.length;j++){
		// 		if(fitnesses[i] == temp[j]){
		// 			fittestMembers[i] = j;
		// 		}
		// 	}
		// }
		// return fittestMembers;

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
			// System.out.println(mom_idx);
			RealGenotype child = RealGenotype.breed2(population.get(mom_idx), population.get(dad_idx));
			
			// add mutation
			child.mutate(0.5);

			new_population.add(child);
		}
		for(int i=popCount-noOfSurvivors;i<popCount;i++){
			new_population.add(population.get(i));
		}

		population = new_population;
	}

    public ArrayList<RealGenotype> getPopulation(){
    	return population;
    }





}