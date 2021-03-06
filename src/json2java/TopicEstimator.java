
package json2java;

import java.io.*;

import jgibblda.*;

/*
 *
 * @author Nikos Stasinopoulos <nstasinopoulos@gmail.com>
 * @version     Oct 12, 2010
 * @since       1.6
 */
public class TopicEstimator {

    /**
     *Class for running LDA and creating topics out of tickets
     *
     * @param nTopics Number of topics to be calculated by LDA
     *
     * @param directory Locations of keywords.txt file
     *
     * @param alpha Alpha hyper-parameter
     * 
     * @param beta Bet hyper-parameter
     *
     * @param niters Number of iterations for the model creator
     */
    public void runLDA(int nTopics, String directory, double alpha, double beta, int niters) {

        System.out.println("Starting LDA for discovering " + nTopics + " topics in " + directory + "keywords.txt");
        LDACmdOption option = new LDACmdOption();
        option.est = true;

        option.alpha = alpha;
        option.beta = beta;

        option.K = nTopics;
        option.niters = niters;
        option.savestep = 2000;
        option.dir = directory;
        option.twords = 20;
        option.dfile = "keywords.txt";

        System.out.println("Gibbs LDA Parameters:");
        System.out.println("alpha:\t" + option.alpha);
        System.out.println("beta:\t" + option.beta);
        System.out.println("Topics:\t" + option.K);
        System.out.println("Iterations:\t" + option.niters);
        System.out.println("savestep:\t" + option.savestep);
        System.out.println("Topic Words:\t" + option.twords);
        System.out.println("dfile:\t" + option.dfile);


        try {

            if (option.est || option.estc) {
                if (option.est) {
                    System.out.println("Estimate the LDA model from scratch");
                } else {
                    System.out.println("Continue to estimate the model from a previously estimated model");
                }
                Estimator estimator = new Estimator();
                estimator.init(option);
                estimator.estimate();
            } else if (option.inf) {
                System.out.println("Do inference for previously unseen (new) data using a previously estimated LDA model");
                Inferencer inferencer = new Inferencer();
                inferencer.init(option);

                Model newModel = inferencer.inference();

                for (int i = 0; i < newModel.phi.length; ++i) {
                    //phi: K * V
                    System.out.println("-----------------------\ntopic" + i + " : ");
                    for (int j = 0; j < 10; ++j) {
                        System.out.println(inferencer.globalDict.id2word.get(j) + "\t" + newModel.phi[i][j]);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error in main: " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    /**
     * Returns a ticket (document) <--> topic matrix containing probabilities
     *
     * @param nTopics Number of topics calculated by LDA
     *
     * @return topicDistribution double[][] matrix with ticket-topic probabilities
     */
    public double[][] LDAThetaReader(int nTopics) {
        BufferedReader br = null;
        double[][] topicDistribution = new double[new NGDCalculator().determineLinesNumberofFile("model-final.theta")][nTopics];

        try {
            String sCurrentLine;
            File f = new File("model-final.theta");
            if (!f.exists()) {
                return topicDistribution;
            }
            br = new BufferedReader(new FileReader(f));
            int lineIndex = 0;
            while ((sCurrentLine = br.readLine()) != null) {
                String line = sCurrentLine;
                line = line.trim();

                for (int i = 0; i < nTopics; i++) {
                    topicDistribution[lineIndex][i] = Double.parseDouble(line.split(" ")[i].trim());//parse theta and convert to double
                }
                lineIndex++;
            }

//            for (int jj = 0; jj < topicDistribution.length; jj++) {
//                System.out.println(topicDistribution[jj][0] + "\t" + topicDistribution[jj][1]);
//            }

            return topicDistribution;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println(e.getMessage());
            }
//            String[] bugs = new String[determineLinesNumberofFile("buglist.txt")];

            return topicDistribution;

        }
    }

    /**
     *Deprecated.<br>Compares probabilities and return most probable topic for each ticket
     *
     * @param topicDistribution Theta distribution matrix
     */
    public void topicLDADecider(double[][] topicDistribution){

        for (int jj = 0; jj < topicDistribution.length; jj++) {
                if ( topicDistribution[jj][0] >= topicDistribution[jj][1]){
                }
            }
    }
}
