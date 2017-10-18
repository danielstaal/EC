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
    static int                   populationSize_     = 20;
    static int                   NO_VARIABLES        = 10;
    double                       maxPopDistance      = 0.5; // maximum distance to population centroid
    boolean                      fitnessSharing      = true;
    
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

    
    public Population(int evaluationsLimit, ContestEvaluation evaluation, Map evaluationType, int popSize, float maxPopD)
    {
        evaluation_       = evaluation;
        evaluationType_   = evaluationType;
        evaluationsLimit_ = evaluationsLimit;
        evaluations       = 0;
        fitness_          = 0;

        // setting passed arguments
        populationSize_ = popSize;
        maxPopDistance = maxPopD;

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
        }
        sortPopulation();
        return true;
    }

    
    public void sortPopulation(){
        Collections.sort(population_, (o2, o1) ->  Double.compare(o1.fitness_, o2.fitness_));}

    
    public void speciate(){
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
        // Remove empty species.
        species_.removeIf(sp -> sp.members_.isEmpty());
        // Assign random prototype to each species.
        for(Species sp : species_){
            sp.prototype_ = sp.members_.get(r.nextInt(sp.members_.size()));
        }
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
        for(int sign=Integer.signum(populationSize_ - nExpectedOffspring); populationSize_
                != nExpectedOffspring; ){
            species_.get(r.nextInt(species_.size())).nOffspring_ += sign;
            nExpectedOffspring += sign;
        }
    }

    
    public void generateNextGen(){
        population_.clear();
        for(int i=0; i<species_.size(); i++){
            species_.get(i).sort();
            species_.get(i).generateOffspring();
            population_.addAll(species_.get(i).members_);
        }
    }
}
