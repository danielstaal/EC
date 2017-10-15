import itertools
from subprocess import call








if __name__ == '__main__':
    
    populationSize = ['1','2','3','4']
    speciation = ['true', 'false']
    alpha = ["0.1", '0.2', '0.3', '0.4', '0.5']
    sigma = ['x', 'y', 'z']

    hyperparams = [populationSize, speciation, alpha, sigma]


    combinations = list(itertools.product(*hyperparams))

    # print(combinations[0:5])

    call(['export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/'])
    



    # for comb in combinations:



