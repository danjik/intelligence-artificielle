package voyageurCommerce;
import java.util.ArrayList;

/**
 * Created by Nicolas on 26/02/2016.
 */
public class Individu {
    private ArrayList<Integer> circuit;
    private int evaluation;
    private int numero;

    public Individu() {
        circuit = new ArrayList<Integer>();
    }

    public void addNoeud(int n) {
        circuit.add(n);
    }

    public Individu croisement(ArrayList<Integer> circuitPartiel) {
        ArrayList<Integer> tmpCircuit = new ArrayList<Integer>();

        for(int i=0 ; i<circuit.size() ; i++) {
            tmpCircuit.add(circuit.get(i));
        }

        //On enlève les éléments de circuitPartiel du circuit actuel
        for(int i=0 ; i<circuitPartiel.size() ; i++) {
            int aEnlever = circuitPartiel.get(i);

            for(int j=0 ; j<tmpCircuit.size() ; j++) {
                if(tmpCircuit.get(j) == aEnlever) {
                    tmpCircuit.remove(j);
                    break;
                }
            }
        }

        //Puis on concatène
        for(int i=0 ; i<circuitPartiel.size() ; i++) {
            tmpCircuit.add(circuitPartiel.get(i));
        }

        Individu retour = new Individu();

        for(int i=0 ; i<tmpCircuit.size() ; i++) {
            retour.addNoeud(tmpCircuit.get(i));
        }

        return retour;
    }

    public ArrayList<Integer> getCroisementPart(int debut, int fin) {
        if(debut < 0 || debut >= fin) return null;
        if(fin >= circuit.size()) return null;

        return new ArrayList<Integer>(circuit.subList(debut, fin + 1));
    }

    public void mutation () {
        //On tire des entiers pour inverser
        int i1 = (int)(Math.random() * circuit.size());
        int i2 = 0;
        do {
            i2 = (int)(Math.random() * circuit.size());
        } while (i2 == i1);

        int tmp = circuit.get(i1);
        circuit.set(i1, circuit.get(i2));
        circuit.set(i2, tmp);
    }

    public void afficher() {
        if(circuit.size() == 0) return;

        System.out.print(Integer.toString(circuit.get(0)));

        for(int i=1 ; i<circuit.size() ; i++) {
            System.out.print(" -> " + Integer.toString(circuit.get(i)));
        }

        System.out.println();
    }

    public boolean equals(Object o) {
        if(!(o instanceof Individu)) return false;

        ArrayList<Integer> circuit2 = ((Individu) o).getCircuit();

        if(circuit.size() != circuit2.size()) return false;

        for(int i=0;i<circuit.size();i++) {
            if(circuit.get(i) != circuit2.get(i)) return false;
        }

        return true;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public ArrayList<Integer> getCircuit() {
        return circuit;
    }
}
