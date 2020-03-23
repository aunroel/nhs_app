package com.uk.ac.ucl.carefulai;


public class AnonymousData {
    int score;

    public AnonymousData(int score) {
        this.score = randomize(score);
    }

    private int randomize(int score){
        int answer = score;
        double randomNumber = Math.random() * 100;
        if(randomNumber > 70){
            double randscore = Math.random() * 11 * 10000;
            answer = (int)(randscore);
            answer /= 10000;
        }
        return answer;
    }

    public int getScore() {
        return score;
    }

}
