package com.uk.ac.ucl.carefulai;

//Anonymises the data for sending using Local Differential Privacy

public class AnonymousData {
    int score;

    //on construction, anonymise the score
    AnonymousData(int score) {
        this.score = randomize(score);
    }

    private int randomize(int score){ //randomise the score given
        int answer = score; //copy the score
        double randomNumber = Math.random() * 100; //random number between 1 and 100
        if(randomNumber > 70){ //if the number is above 70 (30% probability of this), generate a random score between 1 and 10
            double randscore = Math.random() * 11 * 10000;
            answer = (int)(randscore);
            answer /= 10000;
        }
        return answer;
    }

    public int getScore() {
        return score;
    } //get the anonymised score

}
