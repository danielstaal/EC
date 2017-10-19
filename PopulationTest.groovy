
class PopulationTest extends GroovyTestCase {
    void testGetMostSimilarSpecies() {
        ArrayList<Species> species_ = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            species_.add(new Species(new Genotype(Population.NO_VARIABLES)))
        }
        Genotype individual = new Genotype(Population.NO_VARIABLES)
        print("individual:\n" + individual + "\n")
        double min_dist = Double.MAX_VALUE
        double distance
        for(int i = 0; i < species_.size(); i++){
            distance = Population.distance(individual,
                    species_.get(i).prototype_)
            System.out.printf('prototype %d:\n%s\ndistance to individual: %f\n',
            i, species_.get(i).prototype_, distance)
            if(distance < min_dist){
                min_dist = distance
            }
            //print('prototype %d:\n%s\ndistance to individual: %d'.
            //        format(i, species_.get(i).prototype_.toString(),
            //                Population.distance(individual,
            //                        species_.get(i).prototype_)))
        }
        Species best_match = Population.getMostSimilarSpecies(individual, species_)
        System.out.printf("best match is with prototype:\n%s", best_match.prototype_)
        assert min_dist == Population.distance(individual, best_match.prototype_)
    }
}
