import java.util.ArrayList;
import java.util.Random;

public class Population
{
	private int popCount;
	private ArrayList<RealGenotype> population = new ArrayList<RealGenotype>();

	public Population(int popcount){
		popCount = popcount;
		createPopulation();
	}

	private void createPopulation(){
		for(int i=0;i<popCount;i++){
        	//population.add(RealGenotype);
        }
	}

    private double[] createMember(){
		Random rnd_ = new Random();
    	double[] member = new double[RealGenotype.D];
    	for(int i = 0; i < RealGenotype.D; i++) {
    		member[i] = rnd_.nextDouble();
		}
		return member;
    }

    public ArrayList<RealGenotype> getPopulation(){
    	return population;
    }








}