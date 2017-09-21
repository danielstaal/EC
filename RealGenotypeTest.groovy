/**
 * Created by fabigato on 21-9-17.
 */
class RealGenotypeTest extends GroovyTestCase {

    void testCreation() {
        int populationSize = 1000;
        RealGenotype[] genes = new RealGenotype[populationSize];
        for(int i = 0; i < populationSize; i++){
            genes[i] = new RealGenotype();
            double[] genotype = genes[i].getValue();
            for(int j = 0; j < genotype.length; j++){
                assert(genotype[j] >= RealGenotype.DOMAIN_LO && genotype[j] <= RealGenotype.DOMAIN_HI)
            }
        }
    }

    void testMutate() {

    }

    void testMutate1() {

    }

}
