import java.util.Random;
public class Genotype
{

    /********************
     *  hyperparameters  *
     *********************/
    public static int           NO_VARIABLES;
    public static double        DOMAIN_HI        = 5;
    public static double        DOMAIN_LO        = -5;
    public double               mutationP        = 4.0/NO_VARIABLES; // probability that an allel gets mutated
    private double              mutationStdStart = 0.05;  // mutation standard dev. at start
    private double              mutationStdEnd   = 0.0001; // mutation standard dev. at finish

    /********************
     *   local variables *
     *********************/
    double[]          genome_;
    double            fitness_;
    int               species_;
    static Random     r;
    
    public Genotype(int noVariables)
    {
        NO_VARIABLES   = noVariables;
        genome_       = new double[NO_VARIABLES];
        fitness_       = 0;
        species_       = 0;
        r              = new Random();
        init_genome();
    }

    private void init_genome(){
        for(int i = 0; i<NO_VARIABLES; i++){
            genome_[i] = (DOMAIN_HI-DOMAIN_LO)* r.nextDouble() + DOMAIN_LO;
        }
    }
    
    /**
     * Takes two Genotypes that act as parents and returns a Genotype kid that is a combination of both parents.
     * An allel of a parent is selected with a probability proportional to the fitness of that parent. 
     **/
    public static Genotype breed(Genotype mom,Genotype dad)
    {
        Genotype kid = new Genotype(NO_VARIABLES);
        for(int i=0; i<kid.genome_.length; i++){
            if(r.nextDouble() <= mom.fitness_/(mom.fitness_+dad.fitness_)){
                kid.genome_[i] = mom.genome_[i];
            }else{
                kid.genome_[i] = dad.genome_[i];
            }
        }
        return kid;
    }
    
    /**
     * A quantity (with mean 0 and standard deviation mutationStd)is added to every allel with probability 
     * mutationP.
     **/
    public Genotype mutate(){
        Random r = new Random();
        double mutationStd = mutationStdStart *
            (1 - ((double) Population.evaluations / Population.evaluationsLimit_))
            + mutationStdEnd * ((double) Population.evaluations / Population.evaluationsLimit_);
        for(int i = 0; i < NO_VARIABLES; i++){
            if(r.nextDouble()<mutationP){
                double increment;
                do{
                    increment = r.nextGaussian()*mutationStd;
                } while (Math.abs(genome_[i] + increment)>DOMAIN_HI);
                genome_[i] += increment;
            }
        }
        return this;
    }    
}