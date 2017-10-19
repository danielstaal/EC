import itertools
import subprocess
import sys
import os


if __name__ == '__main__':

    # compile
    subprocess.run(['javac', '-cp', 'contest.jar', 'group39.java', 'Genotype.java', 'Population.java', 'Species.java'])
    subprocess.run(['jar', 'cmf', 'MainClass.txt', 'submission.jar', 'group39.class', 'Genotype.class', 'Population.class', 'Species.class'])
    
    ## list of each hyperparameter value we want to test
    populationSize = ['5','20','50','100']
    maxPopDistance = ['0.2','0.4', '0.6', '0.8', '1.0']
    # fitnessSharing = ['true', 'false']
    mutationP = ['0.2']#,'0.4', '0.6', '0.8', '1.0']
    mutationStdStart = ['0.2']#,'0.4', '0.6', '0.8', '1.0']
    mutationStdEnd = ['0.2']#,'0.4', '0.6', '0.8', '1.0']

    # list of hyperparameters 
    hyperparams = [populationSize, maxPopDistance, mutationP, mutationStdStart, mutationStdEnd]
    hyperparams_names = ['populationSize', 'maxPopDistance', 'mP', 'mStdStart', 'mStdEnd']
    evaluationType = 'BentCigarFunction'
    # evaluationType = 'KatsuuraEvaluation'
    # evaluationType = 'SchaffersEvaluation'



    # make all permutations of the values of the hyperparams
    combinations = list(itertools.product(*hyperparams))

    # open new results file
    file_id = 0
    file_name = "results/" + evaluationType + "_"
    while os.path.isfile(file_name + str(file_id) + ".txt"):
        file_id += 1
    file_name += str(file_id) + ".txt"
    
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
        java_exe_line.extend(["-jar","testrun.jar", "-submission=group39", "-evaluation=" + evaluationType, "-seed=1"])

        test = subprocess.Popen(java_exe_line, stdout=subprocess.PIPE)
        output = test.communicate()[0]

        output_string = output.decode("utf-8")

        write_output = output_string.split("\n")
        score = write_output[0]
        runtime = write_output[1][:-1]

        f.write(score.split(':')[1][1:] + runtime.split(':')[1][:-1] + " " + argumentValues + '\n')

    f.close()
