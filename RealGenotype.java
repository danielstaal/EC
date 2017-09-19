import java.util.Random;

/**
 * Created by fabigato on 16-9-17.
 * A genotype object with all the methods to breed, mutate and related genotype stuff
 */
public class RealGenotype {
    public enum MutationType{

    }
    public static final int D = 10; //solution space dimensionality
    private double[] value;
    private float mutationP = 1/RealGenotype.D;
    /**
     * Empty constructor. Creates a genotype with double values uniformly distributed in [0, 1]
     * */
    public RealGenotype() {
        Random r = new Random();
        value = new double[RealGenotype.D];
        for(int i = 0; i < RealGenotype.D; i++){
            value[i] = r.nextFloat();
        }
    }
    /**
     * Creates a genotype with double values uniformly distributed in [valueRangeLo, valueRangeHi]
     * */
    public RealGenotype(float valueRangeLo, float valueRangeHi){
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
    //TODO: check this funciton
    public RealGenotype mutate(double lo, double hi){
        Random r = new Random();
        for(int i = 0; i < RealGenotype.D; i++){
            if(r.nextFloat() < this.mutationP)
                this.value[i] = (hi - lo)*r.nextDouble() + lo;
        }
        return this;
    }
    public static void main(String[] args){
        System.out.println("Testing Genotype");
        RealGenotype[] genes = new RealGenotype[10];
        for(int i = 0; i < 10; i++){
            genes[i] = new RealGenotype(-100, 100);
            System.out.println(genes[i]);
        }
        System.exit(1);
    }
}
