import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

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
		System.out.println(population.get(0).getValue());
		System.out.println(population.get(1).getValue());
		System.out.println(population.get(2).getValue());
		// System.out.println(Arrays.toString(sel));
		// recombination, mutation
		// recombination(sel)

		// birth and death


	}

	private void selection(){
		// double[] fitnesses = new double[popCount];
		for(int i=0;i<popCount;i++){
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

		population.arrayList.sort(Comparator.comparing(RealGenotype::getFitness));
	}

	// private void recombination(int[] selectedIndividuals){
	// 	for(int i=0;i<popCount;i++){
	// 		if()
	// 	}
	// }

    public ArrayList<RealGenotype> getPopulation(){
    	return population;
    }





}