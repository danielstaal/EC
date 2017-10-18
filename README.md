# EC
compile:
javac -cp contest.jar player39.java
create jar:
jar cmf MainClass.txt submission.jar player39.class
run:
java -jar testrun.jar -submission=player39 -evaluation=BentCigarFunction -seed=1

if needed, add libcjavabbob.so to path:
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.libcjavabbob.so
