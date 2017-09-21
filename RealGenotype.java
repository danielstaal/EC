import java.util.Random;

/**
 * Created by fabigato on 16-9-17.
 * A genotype object with all the methods to breed, mutate and related genotype stuff
 */
public class RealGenotype {
    public enum MutationType{

    }
    public static final int D = 10; //solution space dimensionality
    public static final double DOMAIN_HI = 5;
    public static final double DOMAIN_LO = -5;
    private double[] value;
    private double fitness;
    //non static, so that different individuals can mutate at different rates
    private float mutationP = 1/RealGenotype.D;
    /**
     * Empty constructor. Creates a genotype with double values uniformly distributed in [0, 1]
     * */
    public RealGenotype() {
	this.fitness = -1;
        Random r = new Random();
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
            System.out.println("Invalid range. Upper: " + valueRangeHi + ". Lower: " + valueRangeLo);
            System.out.println("Using default bounds");
            valueRangeHi = RealGenotype.DOMAIN_HI;
            valueRangeLo = RealGenotype.DOMAIN_LO;
        }
        Random r = new Random();
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
    public void setMutationP(float newP){this.mutationP=newP;}
    public float getMutationP(){return this.mutationP;}
    public double[] getValue(){return value;}
    public double getFitness(){return this.fitness;}
    public void setFitness(double fitness){this.fitness = fitness;}
    /**
     * Uniform mutation. Each allele has mutationP probability of being changed to a new value, uniformly
     * distributed in [lo, hi]
     * */
    public RealGenotype mutate(){
        Random r = new Random();
        for(int i = 0; i < RealGenotype.D; i++){
            if(r.nextFloat() < this.mutationP)
                this.value[i] = (RealGenotype.DOMAIN_HI - RealGenotype.DOMAIN_LO)*r.nextDouble() + RealGenotype.DOMAIN_LO;
        }
        return this;
    }
    /**
     * Creep (gaussian) mutation. Every allele is added a random normal distributed number with sigma
     * deviation
     * */
    public RealGenotype mutate(double sigma){
        Random r = new Random();
        for(int i = 0; i < RealGenotype.D; i++){
            do{
                this.value[i] += r.nextGaussian()*sigma;
               } while (Math.abs(this.value[i])>5);
        }
        return this;
    }
    /** 
     * 1-point crossover breeding function
     **/
    public static RealGenotype breed1(RealGenotype mom, RealGenotype dad){
        Random r = new Random();
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
        Random r = new Random();
        for(int i=0; i<kid.getValue().length; i++){
            if(r.nextDouble() <= mom.getFitness()/(mom.getFitness()+dad.getFitness())){
            kid.getValue()[i] = mom.getValue()[i];
            } else{
            kid.getValue()[i] = dad.getValue()[i];
            }
        }
        return kid;
    }
}













