package colonieFourmis;

import java.util.ArrayList;

import voyageurCommerce.Ville;

/**
 * Created by Nicolas on 20/04/2016.
 */
public class Fourmi {
    private static final int STATE_IDLE = 0;
    private static final int STATE_MOVE = 1;
    private static final int STATE_FINISHED = 2;
    private static final int STATE_NEW_CIRCUIT = 3;

    //private int avancement;
    private AlgorithmeFourmis algorithmeFourmis;
    private ArrayList<Ville> villes;
    private ArrayList<Integer> restants;
    private ArrayList<Integer> circuit;
    private ArrayList<Integer> savedCircuit;
    private int ville;
    private int target;
    private double x;
    private double y;
    private int progres;
    private int state;
    private boolean newCircuit;
    private int valeurCircuit;
    private int pheromones;
	private int speed;

    public Fourmi(ArrayList<Ville> listeVilles, AlgorithmeFourmis algorithme) {
    	this.speed = 10;
        algorithmeFourmis = algorithme;
        restants = new ArrayList<Integer>();
        for(int i = 0 ; i < listeVilles.size() ; i++) {
            restants.add(i);
        }
        circuit = new ArrayList<Integer>();
        villes = listeVilles;
        //On tire au sort la ville de départ
        int depart = (int)(Math.random() * villes.size());
        x = villes.get(depart).getX();
        y = villes.get(depart).getY();
        ville = depart;
        circuit.add(depart);
        restants.remove(new Integer(ville));
        newCircuit = false;
        state = STATE_IDLE;
    }

    public void iterate() {
        switch(state) {
            case STATE_IDLE:
                progres =0;
                if(restants.size() == 0) {
                    state = STATE_FINISHED;
                    break;
                }
                do {
                    //On récupère la liste des intensités de phéromones
                    int somme = 0;
                    ArrayList<Integer> intensites = algorithmeFourmis.getPheromones(ville, restants);
                    ArrayList<Double> probas = new ArrayList<Double>();
                    for(int i=0 ; i<intensites.size() ; i++) {
                        somme += intensites.get(i);
                    }
                    for(int i=0 ; i<intensites.size() ; i++) {
                        probas.add(((double)intensites.get(i) / somme));
                    }
                    double tirage = Math.random();
                    double current = 0;
                    int parcours;
                    for(parcours = 0 ; parcours<intensites.size() ; parcours++) {
                        current += probas.get(parcours);
                        if(current >= tirage) break;
                    }
                    target = restants.get(parcours);
                } while (target == ville);
                circuit.add(target);
                restants.remove(new Integer(target));
                state = STATE_MOVE;
                break;
            case STATE_MOVE:
                progres += speed;
                if(progres >100)progres =100;
                x = villes.get(ville).getX() + (villes.get(target).getX() - villes.get(ville).getX()) * (progres/100.0);
                y = villes.get(ville).getY() + (villes.get(target).getY() - villes.get(ville).getY()) * (progres/100.0);
                if(progres >= 100) {
                    state = STATE_IDLE;
                    ville = target;
                }
                break;
            case STATE_FINISHED:
                //Cas ou on a terminé le circuit
                //On revient en arrière
                if(circuit.size() == villes.size()) {
                    newCircuit = true;
                    valeurCircuit = evaluerCircuit();
                    savedCircuit = new ArrayList<Integer>(circuit);
                    //On doit mettre à jour les phéromones
                    /*int reference;
                    if(AlgorithmeFourmis.EVAL_MIN == Integer.MAX_VALUE) {
                        reference = 2000;
                    }
                    else reference = AlgorithmeFourmis.EVAL_MIN;

                    if (valeurCircuit < reference) reference = valeurCircuit;

                    pheromones = 3000 - (valeurCircuit - reference);
                    if(pheromones < 0) pheromones = 0;
                    pheromones = (pheromones * 30) / 3000;*/

                    if(AlgorithmeFourmis.EVAL_MAX == 0) {
                        pheromones = 10000 / valeurCircuit;
                    }
                    else pheromones = (int)(((double)AlgorithmeFourmis.EVAL_MAX / valeurCircuit) * (AlgorithmeFourmis.EVAL_MAX - valeurCircuit));
                }
                if(circuit.size() == 0) {
                    state = STATE_NEW_CIRCUIT;
                    break;
                }
                target = circuit.get(circuit.size() - 1);
                algorithmeFourmis.updatePheromones(ville, target, pheromones);
                circuit.remove(circuit.size() - 1);
                state = STATE_MOVE;
                break;
            case STATE_NEW_CIRCUIT:
                circuit = new ArrayList<Integer>();
                circuit.add(ville);
                for(int i = 0 ; i < villes.size() ; i++) {
                    restants.add(i);
                }
                restants.remove(new Integer(ville));
                state = STATE_IDLE;
                break;
        }
    }

    private int evaluerCircuit() {
        double somme = 0;
        for(int i=0 ; i<circuit.size() - 1 ; i++) {
            somme += distance(villes.get(circuit.get(i)), villes.get(circuit.get(i+1)));
        }

        return (int)somme;
    }

    private double distance(Ville v1, Ville v2) {
        return Math.sqrt((v1.getX() - v2.getX()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v1.getY() - v2.getY()));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isNewCircuit() {
        return newCircuit;
    }

    public ArrayList<Integer> getSavedCircuit() {
        return savedCircuit;
    }

    public int getValeurCircuit() {
        newCircuit = false;
        return valeurCircuit;
    }

	public void setSpeed(int speed) {
		this.speed = speed;
		
	}
}
