class PopulationTest extends GroovyTestCase {
    void testNextGeneration() {
    }

    void testMutate() {
    }

    void testGetPopulation() {
    }

    void testGetNoOfGenerations() {
    }

    void testSetFitness_sharing() {
    }

    void testSetFSSigmaDistance() {
        ArrayList<RealGenotype> lista = new ArrayList<>();
        RealGenotype gen = new RealGenotype(-5, 5);
        print("original object hash: " + System.identityHashCode(gen));
        ArrayList<RealGenotype> otraLista = new ArrayList<>();
        lista.add(gen);
        print("lista object hash: " + System.identityHashCode(lista.get(0)));
        otraLista.add(lista.get(0));
        print("lista object hash: " + System.identityHashCode(otraLista.get(0)));
    }
}
