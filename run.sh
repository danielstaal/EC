export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.libcjavabbob.so

javac -cp contest.jar player39.java Genotype.java Population.java Species.java
jar cmf MainClass.txt submission.jar player39.class Genotype.class Population.class Species.class


java -jar testrun.jar -submission=player39 -evaluation=KatsuuraEvaluation -seed=1
