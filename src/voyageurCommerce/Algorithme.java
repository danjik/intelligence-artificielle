package voyageurCommerce;
import java.util.ArrayList;

/**
 * Created by Nicolas on 26/02/2016.
 */
public class Algorithme {
    private ArrayList<Individu> population; //Etat actuel de la population
    private int tailleCircuit;
    private ArrayList<Ville> villes;
    private ArrayList<Integer> meilleurCircuit;
    private int plusCourt;
	private double PROBA_MUTATION;
	private int EVAL_MIN;
    public static int valMax;
    public static int valMin;

    public Algorithme(int n,double probaMutation, int evalMin) { //Crée un algortihme pour n villes
        tailleCircuit = n;
        population = new ArrayList<Individu>();
        PROBA_MUTATION = probaMutation;
        EVAL_MIN = evalMin;
        plusCourt = Integer.MAX_VALUE;
        meilleurCircuit = null;
    }

    public void addRandomIndividu() {

        boolean ajoute = false;

        while(!ajoute) {
            ArrayList<Integer> tmp = new ArrayList<Integer>();

            for (int i = 0; i < tailleCircuit; i++) {
                tmp.add(i);
            }

            Individu individu = new Individu();

            for (int i = 0; i < tailleCircuit; i++) {
                int index = (int) (Math.random() * tmp.size());
                individu.addNoeud(tmp.get(index));
                tmp.remove(index);
            }

            if(!population.contains(individu)) {
                population.add(individu);
                ajoute = true;
            }
        }
    }

    public int getPopulationSize() {
        return population.size();
    }

    public Individu getIndividu(int n) { //Renvoie l'individu d'index n dans la population
        if(n<0 || n>= population.size()) return null;

        return population.get(n);
    }

    public void iteration(int nb) {
        for(int i=0 ; i<nb ; i++) {
            int nbIndividus = population.size();

            doCroisements(nbIndividus / 2, nbIndividus);
            doMutations(PROBA_MUTATION , nbIndividus);

            doRouletteRusse(nbIndividus);
        }

        //Recherche min et max
    }

    public void doCroisements(int nbCroisements, int nbIndividus) {
        for(int i=0 ; i<nbCroisements ; i++) {
            //Tirage des deux individus
            int i1 = (int) (Math.random() * nbIndividus);
            int i2;
            do {
                i2 = (int) (Math.random() * nbIndividus);
            } while (i2 == i1);

            int part1 = (int) (Math.random() * (tailleCircuit - 3));
            int part2 = (int) (Math.random() * (tailleCircuit - 1 - part1)) + part1 + 1;

            ArrayList<Integer> tmp1 = population.get(i1).getCroisementPart(part1, part2);
            ArrayList<Integer> tmp2 = population.get(i2).getCroisementPart(part1, part2);

            Individu enfant1 = population.get(i1).croisement(tmp2);
            Individu enfant2 = population.get(i2).croisement(tmp1);

            ///*if(!population.contains(enfant1))*/ population.add(enfant1);
            ///*if(!population.contains(enfant2))*/ population.add(enfant2);
            
            if(!population.contains(enfant1)) population.add(enfant1);
            if(!population.contains(enfant2)) population.add(enfant2);
        }
    }

    public void doMutations(double proba, int nbIndividus) {
        for(int i=0 ; i<nbIndividus ; i++) {
            double tirage = Math.random();
            if(tirage < proba) {
                population.get(i).mutation();
            }
        }
    }

    public void doRouletteRusse(int nbIndividus) {
        evaluerIndividus();
        while(population.size() > nbIndividus) {

            //On calcule le min des évals
            int evalMin = Integer.MAX_VALUE;
            for(int i=0 ; i<population.size() ; i++) {
                int eval = population.get(i).getEvaluation();
                if(eval < evalMin) evalMin = eval;
            }

            int reference = evalMin - EVAL_MIN;
            int somme = 0;
            for(int i=0 ; i<population.size() ; i++) {
                somme += (population.get(i).getEvaluation() - reference);
            }

            ArrayList<Double> probas = new ArrayList<Double>();

            for(int i=0 ; i<population.size() ; i++) {
                probas.add((double)(population.get(i).getEvaluation() - reference) / somme);
            }

            double tirage = Math.random();
            double current = 0;

            for(int i=0 ; i<population.size() ; i++) {
                if(tirage >= current && tirage <= current + probas.get(i)) {
                    population.remove(i);
                    break;
                }

                current += probas.get(i);
            }
        }
    }

    public ArrayList<Individu> getPopulation() {
        return population;
    }

    public void setVilles(ArrayList<Ville> villes) {
        this.villes = villes;
    }
    public ArrayList<Ville> getVilles(){
    	return villes;
    }

    public void evaluerIndividus() {

    	valMin = Integer.MAX_VALUE;
    	valMax = 0;
        int idMin = -1;

        for (int i=0 ; i<population.size() ; i++) {
            double somme = 0;
            ArrayList<Integer> circuit = population.get(i).getCircuit();

            int current = circuit.get(0);
            int next;

            for(int j=1 ; j<=circuit.size() ; j++) {
                if(j != circuit.size()) next = circuit.get(j);
                else next = circuit.get(0);

                somme += distance(villes.get(current), villes.get(next));

                current = next;
            }

            if((int)somme < valMin) {
                idMin = i;
                valMin = (int)somme;
            }
            if((int)somme > valMax){
            	valMax = (int)somme;
            }

            population.get(i).setEvaluation((int)somme);
            population.get(i).setNumero(i);
        }

        if(valMin < plusCourt) {
            plusCourt = valMin;
            meilleurCircuit = population.get(idMin).getCircuit();
        }
    }

    private double distance(Ville v1, Ville v2) {
        return Math.sqrt((v1.getX() - v2.getX()) * (v1.getX() - v2.getX()) + (v1.getY() - v2.getY()) * (v1.getY() - v2.getY()));
    }

    public ArrayList<Integer> getMeilleurCircuit() {
        return meilleurCircuit;
    }

	/**
	 * @return the plusCourt
	 */
	public int getPlusCourt() {
		return plusCourt;
	}

}
