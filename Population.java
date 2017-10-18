import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import org.vu.contest.ContestEvaluation;
import java.util.Map;
import java.util.HashMap;
import java.util.Hashtable;
import java.lang.Math;
import java.util.Collections;
import java.util.Arrays;

public class Population
{
    /*********************
     *  hyperparameters  *
     *********************/
    static int                   populationSize_     = 100;
    static int                   NO_VARIABLES        = 10;
    boolean                      fitnessSharing      = false;
    boolean                      speciate            = false;
    double                       maxPopDistance      = 0.3;// maximum distance to population centroid    

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
            for(Species sp : species_){
                if(distance(g, sp.prototype_) <= maxPopDistance){
                    sp.members_.add(g);
                    matched = true;
                    break;
                }
            }
            if(!matched){
                species_.add(new Species(g));
            }
        }
        species_.removeIf(sp -> sp.members_.isEmpty());  // Remove empty species.
    }

    
    // Returns the distance between two Genomes
    public double distance(Genotype a, Genotype b){
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
