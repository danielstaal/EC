export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.libcjavabbob.so

javac -cp contest.jar group39.java Genotype.java Population.java Species.java
jar cmf MainClass.txt submission.jar group39.class Genotype.class Population.class Species.class


java -jar testrun.jar -submission=group39 -evaluation=BentCigarFunction -seed=1