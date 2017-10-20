import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.Math;

class Species
{
    /********************
    *  hyperparameters  *
    *********************/
    double              selectionStd = 0.1;

    /********************
    *  local variables  *
    *********************/
    int                 age_;
    double              fitness_;
    ArrayList<Genotype> members_;
    Genotype            prototype_;
    int                 nOffspring_;
    Random              r            = new Random();
    
    public Species(Genotype prototype)
    {
        age_      = 0;
        fitness_  = 0;
        members_  = new ArrayList<>();
        prototype_ = prototype;
        members_.add(prototype_);
    }

    public void sort(){
        Collections.sort(members_, (o2, o1) ->  Double.compare(o1.fitness_, o2.fitness_));
    }

    public void generateOffspring(){
        Genotype            mom;
        Genotype            dad;
        Genotype            child;
        ArrayList<Genotype> offspring = new ArrayList<>();
        int momIdx;
        int dadIdx;
        for(int i = 0; i < nOffspring_; i++){
            do{
                momIdx = (int) (members_.size() * Math.abs(r.nextGaussian()) *  selectionStd);
            }while(momIdx >= members_.size());
            do{
                dadIdx = (int) (members_.size() * Math.abs(r.nextGaussian()) * selectionStd);
            }while(dadIdx >= members_.size());
            mom = members_.get(momIdx);
            dad = members_.get(dadIdx);
            child = Genotype.breed(mom, dad);
            child.mutate();
            offspring.add(child);
        }
        members_ = offspring;
    }
}
