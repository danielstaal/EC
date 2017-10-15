import itertools
import subprocess








if __name__ == '__main__':

    subprocess.run(['javac', '-cp', 'contest.jar', 'group39.java', 'Genotype.java', 'Population.java', 'Species.java'])
    subprocess.run(['jar', 'cmf', 'MainClass.txt', 'submission.jar', 'group39.class', 'Genotype.class', 'Population.class', 'Species.class'])
    
    populationSize = ['1','2','3','4']
    speciation = ['true', 'false']
    alpha = ["0.1", '0.2', '0.3', '0.4', '0.5']
    sigma = ['x', 'y', 'z']

    hyperparams = [populationSize, speciation, alpha, sigma]
    hyperparams_names = ['populationSize', 'speciation', 'alpha', 'sigma']



    combinations = list(itertools.product(*hyperparams))

    for combination in combinations[:5]:
        java_exe_line = ["java"]
        for i, hyperparam in enumerate(combination):
            java_exe_line.append('-D' + hyperparams_names[i] + "=" + str(hyperparam))
        java_exe_line.extend(["-jar","testrun.jar", "-submission=group39", "-evaluation=BentCigarFunction", "-seed=1"])

        print(java_exe_line)

        subprocess.run(java_exe_line)



        # test = subprocess.Popen(["ping","-W","2","-c", "1", "192.168.1.70"], stdout=subprocess.PIPE)
        # output = test.communicate()[0]


