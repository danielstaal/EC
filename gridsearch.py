import itertools
import subprocess
import sys







if __name__ == '__main__':

    # compile
    subprocess.run(['javac', '-cp', 'contest.jar', 'group39.java', 'Genotype.java', 'Population.java', 'Species.java'])
    subprocess.run(['jar', 'cmf', 'MainClass.txt', 'submission.jar', 'group39.class', 'Genotype.class', 'Population.class', 'Species.class'])
    
    ## list of each hyperparameter value we want to test
    populationSize = ['5','20','50','100']
    # speciation = ['true', 'false']
    maxPopDistance = ['0.2','0.4', '0.6', '0.8', '1.0']
    # fitnessSharing = ['true', 'false']
    # mutationP
    # mutationStdStart
    # mutationStdEnd


    # list of hyperparameters 
    hyperparams = [populationSize, maxPopDistance]
    hyperparams_names = ['populationSize', 'maxPopDistance']
    # evaluationType = 'BentCigarFunction'
    # evaluationType = 'KatsuuraEvaluation'
    evaluationType = 'SchaffersEvaluation'



    # make all permutations of the values of the hyperparams
    combinations = list(itertools.product(*hyperparams))


    f = open("results/" + evaluationType + ".txt", 'w')
    f.write("Score Time(ms) ")
    for name in hyperparams_names:
        f.write(name + " ")
    f.write(evaluationType + "\n")

    for j, combination in enumerate(combinations):

        # print("no_of_iterations = ", j)
        sys.stdout.write("\rno_of_runs = %i" % j)
        sys.stdout.flush()
        java_exe_line = ["java"]
        argumentValues = ''
        for i, hyperparam in enumerate(combination):
            hp = hyperparams_names[i] + "=" + str(hyperparam)
            argumentValues += str(hyperparam) + ' '
            java_exe_line.append('-D' + hp)
        java_exe_line.extend(["-jar","testrun.jar", "-submission=group39", "-evaluation=" + evaluationType, "-seed=1"])

        test = subprocess.Popen(java_exe_line, stdout=subprocess.PIPE)
        output = test.communicate()[0]

        output_string = output.decode("utf-8")

        write_output = output_string.split("\n")
        score = write_output[0]
        runtime = write_output[1][:-1]

        f.write(score.split(':')[1][1:] + runtime.split(':')[1][:-1] + " " + argumentValues + '\n')

    f.close()
