package com.uk.ac.ucl.carefulai;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JavaClassifier {

    static int counter;
    static int score;
    static int output;
    /**
     * the learning rate
     */
    private double rate;
    /**
     * the weight to learn
     */
    private double[][] weights;
    /**
     * the number of iterations
     */
    private int ITERATIONS = 50;

    //static double[] means;

    public JavaClassifier(int n) {
        this.rate = 0.001;
        weights = new double[n][10];
    }

    private static double sigmoid(double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    public static double[] softmax(double[] array) {
        double sum = 0;
        double[] softmaxarr = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            array[i] = Math.exp(array[i]);
            sum += array[i];
        }
        for (int j = 0; j < array.length; j++) {
            softmaxarr[j] = array[j] / sum;
        }
        String strang = "";
        for (int i = 0; i < softmaxarr.length; i++) {
            strang += String.valueOf(softmaxarr[i]);
        }
        //System.out.println("Sum :" + counter + ", " + sum);
        //System.out.println(softmax.toString());
        counter++;
        return softmaxarr;
    }

    public static List<Instance> readDataSet(String file) throws FileNotFoundException {
        List<Instance> dataset = new ArrayList<Instance>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(file));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] columns = line.split(",");

                // skip first column and last column is the label
                int i = 0;
                double[] data = new double[columns.length - 1];
                for (i = 1; i < columns.length; i++) {
                    data[i - 1] = (double) (Float.parseFloat(columns[i]));
                }
                //data = inputstandardise(data);
                int label = Integer.parseInt(columns[0]);
                Instance instance = new Instance(label, data);
                dataset.add(instance);

            }
        } finally {
            if (scanner != null)
                scanner.close();
        }
        return dataset;
    }

    public static double[] inputstandardise(double[] f, double[] mean, double[] standarddev) {
        for (int i = 0; i < f.length; i++) {
            if (f.length > 8) {
                String s = "";
                for (int j = 0; j < f.length; j++) {
                    s += f[i] + ",";
                }
                Log.v("problem:", s);
            }
            f[i] = ((f[i] - mean[i]) / standarddev[i]);
        }
        return f;
    }

    public static List<Instance> normalise(List<Instance> instances, double[] mean, double[] standarddev) {
        for (int i = 0; i < instances.size(); i++) {
            instances.get(i).x = inputstandardise(instances.get(i).x, mean, standarddev);
        }
        return instances;
    }

    public static double[] meancalculator(List<Instance> instances) {
        double[] means = new double[8];
        for (int n = 0; n < 8; n++) {
            means[n] = 0;
        }
        for (int i = 0; i < instances.size(); i++) {
            double[] x = instances.get(i).x;
            for (int a = 0; a < 8; a++) {
                means[a] += x[a];
            }
        }
        for (int j = 0; j < 8; j++) {
            means[j] = means[j] / instances.size();
        }
        return means;
    }

    public static double[] standarddeviationcalc(List<Instance> instances) {
        double[] difference = new double[8];
        double[] sum = new double[8];
        double[] means = meancalculator(instances);                 //calculate mean of dataset
        for (int i = 0; i < instances.size(); i++) {
            double[] x = instances.get(i).x;
            for (int a = 0; a < 8; a++) {
                difference[a] = x[a] - means[a];                    //difference between x and mean
                sum[a] += (difference[a] * difference[a]);            //summing the squares of the differences
            }
        }
        for (int j = 0; j < 8; j++) {
            sum[j] = sum[j] / instances.size();                       //divide by size of dataset
            sum[j] = Math.sqrt(sum[j]);                             //square root everything
        }
        return sum;
    }

    public static int score(double[] classification) {
        double max = -1;
        output = 0;
        for (int k = 0; k < classification.length; k++) {
            System.out.print((k + 1) + ": " + classification[k] + ",");
            if (classification[k] > max) {
                max = classification[k];
                output = k + 1;
                //System.out.println("Output is now, " + String.valueOf(output));
            }
        }
        return output;
    }

    public void train(List<Instance> instances) {
        for (int n = 0; n < ITERATIONS; n++) {
            double lik = 0.0;
            //System.out.println("instances size" + instances.size());
            for (int i = 0; i < instances.size(); i++) {
                double[] x = instances.get(i).x;
                double[] predicted = classify(x);
                double[] label = onehotencoder(instances.get(i).label);
                double[] difference = new double[predicted.length];
                for (int a = 0; a < difference.length; a++) {
                    difference[a] = label[a] - predicted[a];
                }

                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 10; k++) {
                        weights[j][k] = weights[j][k] + rate * (difference[k]) * x[j];
                    }
                }
                // not necessary for learning
                //lik += label * Math.log(classify(x)) + (1-label) * Math.log(1- classify(x));
            }
        }
    }

    public double[] classify(double[] x) {
        double[] logit = new double[10];
        for (int j = 0; j < 10; j++) {
            for (int i = 0; i < 8; i++) {
                logit[j] += weights[i][j] * x[i];
            }
        }
        double[] result = softmax(logit);
        return result;
    }

    public double[] onehotencoder(int input) {
        double[] array = new double[10];
        if (input > 0 && input <= 10) {
            array[input - 1] = 1;
        } else {
            array[0] = -1;
        }
        return array;
    }

    public static class Instance {
        public int label;
        public double[] x;

        public Instance(int label, double[] x) {
            this.label = label;
            this.x = x;
        }
    }
}
