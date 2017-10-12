import java.util.ArrayList;
import java.util.Random;
import org.vu.contest.ContestEvaluation;
import java.util.Collections;
import java.util.Arrays;

public class Population
{
    private ArrayList<RealGenotype> population = new ArrayList<>();
    private int noOfSurvivors = 0;
    private String evaluationType = "";
    private double evaluationLimit = 0.0;
    ContestEvaluation evaluation_;
    private boolean FITNESS_SHARING = false;
    private double fSSigmaDistance = 1; // neighborhood radius for fitness sharing
    private double fSAlpha = 1; // alpha parameter of fitness sharing. Controlls how much
    // do neighbors punish fitness, set in ]0, inf[
    private double[][] fSDistances;
    private double[] fSFitness;

    private static Random r = new Random();

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
            populationSize = 10;
            selection_std = 0.3;
            noOfGenerations = (int)(evaluationLimit/populationSize);
            noOfSurvivors = 2;
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
        if(this.FITNESS_SHARING)
            this.initfS();
    }
    /**
     * Initializes all fitness sharing related variables
     */
    private void initfS(){
        this.fSDistances = new double[populationSize][populationSize];
        this.fSFitness = new double[populationSize];
        this.fSCalculateFitness();
    }

    public void nextGeneration(){
        // Selection
        selection();
        // System.out.println(Arrays.toString(sel));
        // recombination, mutation
        recombine();
        mutate();
    }

    private void fSCalculateDistances(){
        for(int r = 0; r < populationSize; r++){
            for(int c = 0; c < populationSize; c++){
                this.fSDistances[r][c] = this.fSDistance(this.population.get(r), this.population.get(c));
            }
        }
    }

    private void selection(){
        for(int i=0;i<populationSize-noOfSurvivors;i++){
            population.get(i).setFitness((double) evaluation_.evaluate(population.get(i).getValue()));
        }
        // sort the population according to fitness
        // index 100 is the fittest member
        // population.sort(Comparator.comparing(RealGenotype::getFitness));
        Collections.sort(population,
                (o1, o2) ->  Double.compare(o1.getFitness(), o2.getFitness()));
        if(this.FITNESS_SHARING == true)
                this.fSCalculateFitness();
    }

    private void recombine(){
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
            // System.out.println(mom_idx);
            RealGenotype child = RealGenotype.breed2(population.get(mom_idx), population.get(dad_idx));
                
            new_population.add(child);
        }
        // add parents to the population
        for(int i=populationSize-noOfSurvivors;i<populationSize;i++){
            new_population.add(population.get(i));
        }
        population = new_population;
        if(this.FITNESS_SHARING == true)
            this.fSCalculateFitness();
    }
    
    public void mutate(){
        for(int i=noOfSurvivors;i<populationSize;i++){
            population.get(i).mutate(0.5);
        }
        if(this.FITNESS_SHARING)
            this.fSCalculateFitness();
    }
    
    public ArrayList<RealGenotype> getPopulation(){
        return population;
    }

    public int getNoOfGenerations(){
        return noOfGenerations;
    }

    private void fSCalculateFitness(){
        this.fSCalculateDistances();
        double newFitness;
        ArrayList<Integer> neighbors;
        for(int i = 0; i < this.populationSize; i++){
            newFitness = 0;
            neighbors = this.getFSNeighbors(i);
            for(int n : neighbors){
                newFitness += (1 - Math.pow(this.fSDistances[i][n]/this.fSSigmaDistance, this.fSAlpha));
            }
            newFitness = this.population.get(i).getFitness()/newFitness;
            this.fSFitness[i] = newFitness;
        }
    }

    /**
     * Finds the neighbors of a given member of the population, based on the fSDistance
     * @param i index in the population array of the member of the population whose
     *          neighbors are to be found
     * @return ArrayList with the indices in the population array of the neighbors
     * */
    private ArrayList<Integer> getFSNeighbors(int i){
        ArrayList<Integer> neighbors = new ArrayList<>();
        for(int c = 0; c < this.populationSize; c++){
            if(this.fSDistances[i][c] < this.fSSigmaDistance) {
                neighbors.add(c);
            }
        }
        return neighbors;
    }

    /**
     * calculates euclidean distance between two arrays
     * */
    private double fSDistance(RealGenotype a, RealGenotype b)
    {
        double distance = 0;
        for(int i=0;i<RealGenotype.D;i++){
            distance += Math.pow((a.getValue()[i] - b.getValue()[i]),2);
        }
        return Math.sqrt(distance);
    }

    public void setFitness_sharing(boolean fs){
        this.FITNESS_SHARING = fs;
        if (fs)
            this.initfS();
    }
    public void setFSSigmaDistance(double sigma){
        assert sigma > 0: "fitness sharing sigma must be positive, got " + sigma;
        this.fSSigmaDistance = sigma;
    }
    public void setFSAlpha(double alpha){
        assert alpha > 0: "fitness sharing sigma must be positive, got " + alpha;
        this.fSAlpha = alpha;
    }
}
