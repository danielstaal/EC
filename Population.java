


public class Population
{
	private int popCount;
	private ArrayList<realGenotype> population = new ArrayList<realGenotype>(); 

	public Population(int popcount){
		popCount = popcount;
		createPopulation();
	}

	private createPopulation(){
		for(int i=0;i<popCount;i++){
        	population.add(realGenotype());
        }
	}

    private double[] createMember(){
    	double[] member = new double[dimensions];
    	for(int i = 0; i < dimensions; i++) {
    		member[i] = rnd_.nextDouble();
		}
		return member;
    }

    public ArrayList<realGenotype> getPopulation(){
    	return population;
    }








}