import itertools
import subprocess
import sys
import os


if __name__ == '__main__':

    subprocess.run(['javac', '-cp', 'contest.jar', 'player39.java', 'Genotype.java', 'Population.java', 'Species.java'])
    subprocess.run(['jar', 'cmf', 'MainClass.txt', 'submission.jar', 'player39.class', 'Genotype.class', 'Population.class', 'Species.class'])
    
    ## list of each hyperparameter value we want to test
    populationSize = ['5']#,'20']
    maxPopDistance = ['0.1']#,'0.3', '1.0', '5.0', '10.0']
    mutationP = ['1.0','0.3']#, '0.5']
    mutationStdStart = ['0.1']#,'0.25', '0.4']
    mutationStdEnd = ['0.0001']#,'0.001', '0.01']
    # alpha = []

    fitnessSharing = ['true', 'false']
    speciation = ['true', 'false']

    # list of hyperparameters 
    hyperparams = [populationSize, maxPopDistance, mutationP, mutationStdStart, mutationStdEnd, fitnessSharing, speciation]
    hyperparams_names = ['populationSize', 'maxPopDistance', 'mP', 'mStdStart', 'mStdEnd', 'fitnessSharing', 'speciation']
    
    evaluationType = 'BentCigarFunction'
    # evaluationType = 'KatsuuraEvaluation'
    # evaluationType = 'SchaffersEvaluation'

    # make all permutations of the values of the hyperparams
    combinations = list(itertools.product(*hyperparams))

    # open new results file
    file_id = 0
    file_name = "results/" + evaluationType
    # while os.path.isfile(file_name + str(file_id) + ".txt"):
    #     file_id += 1
    file_name += ".txt"
    
    f = open(file_name, 'w')
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
        java_exe_line.extend(["-jar","testrun.jar", "-submission=player39", "-evaluation=" + evaluationType, "-seed=1"])

        test = subprocess.Popen(java_exe_line, stdout=subprocess.PIPE)
        output = test.communicate()[0]

        output_string = output.decode("utf-8")

        write_output = output_string.split("\n")
        score = write_output[0]
        runtime = write_output[1][:-1]

        f.write(score.split(':')[1][1:] + runtime.split(':')[1][:-1] + " " + argumentValues + '\n')

    f.close()
