export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.libcjavabbob.so

javac -cp contest.jar group39.java RealGenotype.java Population.java
jar cmf MainClass.txt submission.jar group39.class RealGenotype.class Population.class


java -Dtest=1 -Dtest2=2 -jar testrun.jar -submission=group39 -evaluation=BentCigarFunction -seed=1