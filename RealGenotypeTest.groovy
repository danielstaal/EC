/**
 * Created by fabigato on 21-9-17.
 */
class RealGenotypeTest extends GroovyTestCase {

    /**Check individuals created with random values stay within range always*/
    void testCreation() {
        int populationSize = 1000;
        RealGenotype[] genes = new RealGenotype[populationSize];
        for(int i = 0; i < populationSize; i++){
            genes[i] = new RealGenotype();
            double[] genotype = genes[i].getValue();
            for(int j = 0; j < genotype.length; j++){
                assert(genotype[j] >= RealGenotype.DOMAIN_LO && genotype[j] <= RealGenotype.DOMAIN_HI);
            }
        }
    }
    /**Check at least 2/3 of the mutated genes stayed within 1 std, for several stds */
    void testMutateGaussian() {
        double[] sigmas = [1, 2, 3];
        int populationSize = 1000;
        int genotypesWithinSigmaRange = 0;
        int genesWithinSigmaRange = 0;
        RealGenotype[] population = new RealGenotype[populationSize];
        for(double sigma: sigmas) {
            genotypesWithinSigmaRange = 0;
            for(RealGenotype individual : population) {
                individual = new RealGenotype();
                double[] genesBefore = individual.getValue();
                double[] genesAfter = individual.mutate(sigma).getValue();
                genesWithinSigmaRange = 0;
                for (int i = 0; i < RealGenotype.D; i++) {
                    if (Math.abs(genesBefore[i] - genesAfter[i]) <= sigma)
                        genesWithinSigmaRange++;
                }
                if (genesWithinSigmaRange >= (2 / 3) * RealGenotype.D)
                    genotypesWithinSigmaRange++;
            }
            assert(genotypesWithinSigmaRange >= (2/3)*populationSize);
        }
    }

}
