export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/

javac -cp contest.jar group39.java RealGenotype.java Population.java
jar cmf MainClass.txt submission.jar group39.class RealGenotype.class Population.class


java -jar testrun.jar -submission=group39 -evaluation=SchaffersEvaluation -seed=1 3