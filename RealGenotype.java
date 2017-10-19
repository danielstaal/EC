import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by fabigato on 16-9-17.
 * A genotype object with all the methods to breed, mutate and related genotype stuff
 */
public class RealGenotype {

     /**********************
     *  hyperparameters   *
     **********************/
    //non static, so that different individuals can mutate at different rates
    private float              AllelMutationP = 1/RealGenotype.D;

    /**********************
     *  local variables   *
     **********************/

    public enum MutationType{   }
    public static final int    D              = 10; //solution space dimensionality
    public static final double DOMAIN_HI      = 5;
    public static final double DOMAIN_LO      = -5;
    private double[]           value;
    private double             fitness;
    private int                species        = 1;  // species tag
    Random                     r              = new Random();
    
    /**
     * Empty constructor. Creates a genotype with double values uniformly distributed in [0, 1]
     * */
    public RealGenotype() {
        this.fitness = -1;
        value = new double[RealGenotype.D];
        for(int i = 0; i < RealGenotype.D; i++){
            value[i] = (RealGenotype.DOMAIN_HI - RealGenotype.DOMAIN_LO)*r.nextDouble() +
                RealGenotype.DOMAIN_LO;
        }
    }
    /**
     * Creates a genotype with double values uniformly distributed in [valueRangeLo, valueRangeHi]
     * This is an alternative to using the default [RealGenotype.DOMAIN_LO, RealGenotype.DOMAIN_HI] to
     * force the range to something else. Naturally, [valueRangeLo, valueRangeHi] must be contained by
     * [RealGenotype.DOMAIN_LO, RealGenotype.DOMAIN_HI]
     * */
    public RealGenotype(double valueRangeLo, double valueRangeHi){
        this.fitness = -1;
        if(valueRangeHi > RealGenotype.DOMAIN_HI || valueRangeLo < RealGenotype.DOMAIN_LO ||
           valueRangeHi <= RealGenotype.DOMAIN_LO){
            //TODO: do this with a Logger instead: http://www.vogella.com/tutorials/Logging/article.html
            // System.out.println("Invalid range. Upper: " + valueRangeHi + ". Lower: " + valueRangeLo);
            // System.out.println("Using default bounds");
            valueRangeHi = RealGenotype.DOMAIN_HI;
            valueRangeLo = RealGenotype.DOMAIN_LO;
        }
        value = new double[RealGenotype.D];
        for(int i = 0; i < RealGenotype.D; i++){
            value[i] = (valueRangeHi-valueRangeLo)*r.nextDouble() + valueRangeLo;
        }
    }

    /**
     * Copy constructor. Creates a genotype with a given value
     * */
    public RealGenotype(double[] gen){
        this.fitness = -1;
        assert (gen.length == RealGenotype.D);
        for(int i = 0; i < RealGenotype.D; i++){
            assert(RealGenotype.DOMAIN_LO <= gen[i] && gen[i] <= RealGenotype.DOMAIN_HI);
        }
        value = gen;
    }

    @Override
    public String toString(){
        String stringRep = "[";
        for(int i = 0; i < RealGenotype.D; i++){
            stringRep += this.value[i];
            if(i < RealGenotype.D - 1)
                stringRep += " ";
        }
        return stringRep + "]";
    }

    /**
     * Uniform mutation. Each allele has allelMutationP probability of being changed to a new value, uniformly
     * distributed in [lo, hi]
     * */
    public RealGenotype mutate(){
        for(int i = 0; i < RealGenotype.D; i++){
            if(r.nextFloat() < this.allelMutationP)
                this.value[i] = ((RealGenotype.DOMAIN_HI - RealGenotype.DOMAIN_LO) * r.nextDouble()
				 + RealGenotype.DOMAIN_LO);
        }
        return this;
    }

    /**
     * Creep (gaussian) mutation. Every allele is added a random normal distributed number with sigma
     * deviation
     * */
    public RealGenotype mutate(double sigma){
        for(int i = 0; i < RealGenotype.D; i++){
            if(r.nextDouble()<this.allelMutationP){
                double increment;
                do{
                    increment = r.nextGaussian()*sigma;
                } while (Math.abs(this.value[i] + increment)>5);
                this.value[i] += increment;
            }
        }
        return this;
    }

    /** 
     * 1-point crossover breeding function
     **/
    public static RealGenotype breed1(RealGenotype mom, RealGenotype dad){
        int cut = r.nextInt(10);
        RealGenotype kid = new RealGenotype();
        for(int i=0; i<kid.getValue().length; i++){
            if(i<cut){
                kid.getValue()[i] = mom.getValue()[i];
            } else{
                kid.getValue()[i] = dad.getValue()[i];
            }
        }
        return kid;
    }

    /**
     * Takes  two RealGenotypes that act as parents and returns a
     * RealGenotype kid that is a combination of both parents. An allel of a parent
     * is selected with a probability based on the fitness of that parent. 
     * Chance of allel of mom being selected: fitness(mom)/(fitness(mom)+fitness(dad))
     **/
    public static RealGenotype breed2(RealGenotype mom, RealGenotype dad){
        RealGenotype kid = new RealGenotype();
        for(int i=0; i<kid.getValue().length; i++){
            if(r.nextDouble() <= mom.getFitness()/(mom.getFitness()+dad.getFitness())){
                kid.getValue()[i] = mom.getValue()[i];
            } else{
                kid.getValue()[i] = dad.getValue()[i];
            }
        }
        return kid;
    }

    public void setRandomSpecies(int no_of_species){
        species = ThreadLocalRandom.current().nextInt(1, no_of_species + 1);
    }

    public void setMutationP(float newP){this.allelMutationP=newP;}
    public float getMutationP(){return this.allelMutationP;}
    public double[] getValue(){return value;}
    public double getFitness(){return this.fitness;}
    public int getSpecies(){return this.species;}
    public void setSpecies(int species_){this.species=species_;}
    public void setFitness(double fitness){this.fitness = fitness;}

}
