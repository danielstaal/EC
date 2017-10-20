import java.util.ArrayList;
import java.util.Random;
import org.vu.contest.ContestEvaluation;
import java.util.Map;
import java.lang.Math;
import java.util.Collections;

public class Population
{
    /*********************
     *  hyperparameters  *
     *********************/
    static int                   populationSize_     = 20;
    static int                   NO_VARIABLES        = 10;
    boolean                      fitnessSharing      = false;
    boolean                      speciate            = true;
    double                       maxPopDistance      = 0.5;// maximum distance to population centroid    

    /*********************
     *  local variables  *
     *********************/
    private ContestEvaluation    evaluation_;
    private Map                  evaluationType_;
    public static int            evaluationsLimit_;
    public  static int           evaluations;
    public  double               fitness_;
    public ArrayList<Genotype>   population_         = new ArrayList<>();
    public  ArrayList<Species>   species_            = new ArrayList<>();
    static  Random               r                   = new Random();

    public Population(int evaluationsLimit, ContestEvaluation evaluation,
		      Map evaluationType, int popSize, double maxPopD, double mP,
		      double mStdStart, double mStdEnd, boolean fitnessS, boolean speci)
    {

        evaluation_       = evaluation;
        evaluationType_   = evaluationType;
        evaluationsLimit_ = evaluationsLimit;
        evaluations       = 0;
        fitness_          = 0;
        fitnessSharing = fitnessS;
        speciate = speci;

        // setting passed arguments
        populationSize_ = popSize;
        maxPopDistance = maxPopD;
	// fill population
        for(int i = 0; i<populationSize_; i++){population_.add(new Genotype(NO_VARIABLES, mP, mStdStart, mStdEnd));}
    }


    public Population(int evaluationsLimit, ContestEvaluation evaluation, Map evaluationType){

        evaluation_       = evaluation;
        evaluationType_   = evaluationType;
        evaluationsLimit_ = evaluationsLimit;
        evaluations       = 0;
        fitness_          = 0;
        for(int i = 0; i<populationSize_; i++){population_.add(new Genotype(NO_VARIABLES));} //fill population
    }

    
    // Evaluate the fitness of all Species and all their Genotypes.
    public boolean evaluate(){
        fitness_            = 0;
        for(Species sp : species_){
            sp.fitness_     = 0;
            for(Genotype g : sp.members_){
                g.fitness_  = (double) evaluation_.evaluate(g.genome_);
                if(fitnessSharing){
                    g.fitness_ /= sp.members_.size();
                }
                sp.fitness_ += g.fitness_;
                fitness_    += g.fitness_;
                evaluations++;
                if(evaluations >= evaluationsLimit_){
                    return false;
                }
            }
            sp.sort();
	    sp.prototype_ = sp.members_.get(0); // Make the fittest member of a species its prototype
        }
        sortPopulation();
        return true;
    }


    public void sortPopulation(){
        Collections.sort(population_, (o2, o1) ->  Double.compare(o1.fitness_, o2.fitness_));}

    
    public void speciate(){
	if(speciate == false){  //if speciation is turned off, only create one species and add entire population
	    species_.clear();
	    species_.add(new Species(population_.get(0)));
	    species_.get(0).members_.addAll(population_.subList(1, population_.size()));
	    return;
	}
        for(Species sp : species_){  // clear all species (but retain their prototypes)
            sp.members_.clear();
            sp.age_++;
        }
        boolean matched;
        for(Genotype g : population_){
            matched = false;
            if(!species_.isEmpty()) {
                Species best_match = getMostSimilarSpecies(g, species_);
                if (distance(g, best_match.prototype_) <= maxPopDistance) {
                    best_match.members_.add(g);
                    matched = true;
                }
            }
            if(!matched){
                species_.add(new Species(g));
            }
        }
        species_.removeIf(sp -> sp.members_.isEmpty());  // Remove empty species.
    }

    public static Species getMostSimilarSpecies(Genotype individual, ArrayList<Species> species_list){
        return Collections.min(species_list, (s1, s2) ->  Double.compare(
                distance(individual, s1.prototype_),distance(individual, s2.prototype_)));
    }
    
    // Returns the distance between two Genomes
    public static double distance(Genotype a, Genotype b){
        double distance = 0;
        for(int i = 0; i<NO_VARIABLES; i++){
            distance += Math.pow(a.genome_[i] - b.genome_[i], 2);
        }
        distance = Math.sqrt(distance);
        return distance;
    }


    public void calculateNoOffspring(){
        int nExpectedOffspring = 0;
        for(Species sp : species_){
            sp.nOffspring_ = (int) (Math.round(populationSize_ * sp.fitness_ / fitness_));
            nExpectedOffspring += sp.nOffspring_;
        }
        while(populationSize_ != nExpectedOffspring){ //add or subtract offspring until desired popsize is reached
            if(populationSize_ > nExpectedOffspring){
                species_.get(r.nextInt(species_.size())).nOffspring_ += 1;
                ++nExpectedOffspring;
            } else{
                species_.get(r.nextInt(species_.size())).nOffspring_ -= 1;
                --nExpectedOffspring;
            }
        }
    }

    
    public void generateNextGen(){
        population_.clear();
        for(int i=0; i<species_.size(); i++){
            species_.get(i).generateOffspring();
            population_.addAll(species_.get(i).members_);
        }
    }
}
