package colonieFourmis;
import java.util.ArrayList;

import voyageurCommerce.Ville;

/**
 * Created by Nicolas on 26/02/2016.
 */
public class AlgorithmeFourmis {
    private int tailleCircuit;
    private ArrayList<Ville> villes;
    private ArrayList<Fourmi> fourmis;
    private int[][] pheromones;
    public static int EVAL_MIN;
    public static int EVAL_MAX;
    private ArrayList<Integer> meilleurCircuit;

    public AlgorithmeFourmis(int n) { //Crée un algortihme pour n villes
        fourmis = new ArrayList<Fourmi>();
        tailleCircuit = n;
        EVAL_MIN = Integer.MAX_VALUE;
        EVAL_MAX = 0;
    }

    public void ajouterFourmi() {
        fourmis.add(new Fourmi(villes, this));
    }
    public void setSpeedFourmi(int speed){
    	for (Fourmi fourmi : fourmis) {
			fourmi.setSpeed(speed);
		}
    }
    public void randomPheromones() {
        pheromones = new int[tailleCircuit][tailleCircuit];

        for(int i=0;i<tailleCircuit;i++) {
            for(int j=0;j<tailleCircuit;j++) {
                int nb = 1;
                pheromones[i][j] = nb;
            }
        }
    }

    public void iterate() {
        for(int i=0 ; i<fourmis.size() ; i++) {
            fourmis.get(i).iterate();
        }

        if(fourmis.get(0).isNewCircuit()) {
            for(int i=0 ; i<fourmis.size() ; i++) {
                int valeur = fourmis.get(i).getValeurCircuit();
                if(valeur < EVAL_MIN) {
                    EVAL_MIN = valeur;
                    meilleurCircuit = new ArrayList<Integer>(fourmis.get(i).getSavedCircuit());
                }
                if(valeur > EVAL_MAX) {
                    EVAL_MAX = valeur;
                }
            }
            attenuerPheromones();
        }
    }

    private void attenuerPheromones() {
        for(int i=0;i<tailleCircuit;i++) {
            for(int j=0;j<tailleCircuit;j++) {
                pheromones[i][j] = (int)((double)pheromones[i][j] * 0.95);
                if(pheromones[i][j] < 1) pheromones[i][j] = 1;
            }
        }
    }

    public void updatePheromones(int depart, int arrivee, int ajoutPheromones) {
        //pheromones[depart][arrivee] += ajoutPheromones;
        pheromones[depart][arrivee] += (ajoutPheromones * 1000) / distance(villes.get(depart), villes.get(arrivee));
        //if(pheromones[depart][arrivee] > 255) pheromones[depart][arrivee] = 255;
        pheromones[arrivee][depart] = pheromones[depart][arrivee];
    }

    public ArrayList<Integer> getPheromones(int villeDepart, ArrayList<Integer> demandes) {
        ArrayList<Integer> reponse = new ArrayList<Integer>();
        for(int i=0 ; i<demandes.size() ; i++) {
            reponse.add(pheromones[villeDepart][demandes.get(i)]);
        }

        return reponse;
    }

    public int getMaxPheromone() {
        int max = 0;
        for(int i=0 ; i<villes.size() ; i++) {
            for(int j=i+1 ; j<villes.size() ; j++) {
                if(pheromones[i][j] > max) max = pheromones[i][j];
            }
        }

        return max;
    }

    public double getPheromones(int i, int j) {return pheromones[i][j];}

    public int getTailleCircuit() {return tailleCircuit;}

    public ArrayList<Integer> getMeilleurCircuit() {
        return meilleurCircuit;
    }

    public void setVilles(ArrayList<Ville> villes) {
        this.villes = villes;
    }
    public ArrayList<Ville> getVilles(){
    	return villes;
    }

    public ArrayList<Fourmi> getFourmis() {
        return fourmis;
    }

    private double distance(Ville v1, Ville v2) {
        return Math.sqrt((v1.getX() - v2.getX()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v1.getY() - v2.getY()));
    }
}
