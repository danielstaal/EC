def get_highest_score(lines):
    highest_score = [0,0]
    for i, line in enumerate(lines):
        score = float(line.split(" ")[0])
        if score > highest_score[0]:
            highest_score = [score, i]

    print('highest score:', highest_score)

def sort_scores(lines):
    scores = []
    for i, line in enumerate(lines):
            score = float(line.split(" ")[0])

            scores.append(score)

    sorted_ids = list(reversed(sorted(range(len(scores)), key=lambda k: scores[k])))

    for i in sorted_ids[:10]:
        print(lines[i])

if __name__ == '__main__':
    f = open('./results/SchaffersEvaluation.txt', 'r')

    lines = f.readlines()[1:]

    # get_highest_score(lines)

    sort_scores(lines)


