package colonieFourmis;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import voyageurCommerce.Ville;

public class PanelFourmis extends BorderPane {

    private int NB_VILLE = 20;
    
    private ArrayList<Ville> villes;
    private int selectedIndividu;
    private AlgorithmeFourmis algorithme;
    private Canvas canvas;
    private HBox mainPane;
    private AnimationTimer timer;
	private int tmpNbFourmi = 50;
	private int NB_FOURMI;
	private Label labelEvalutation;

    public PanelFourmis(){

        SplitPane splitPane = new SplitPane();

        mainPane = getMainPane();
        VBox settingsPane = getSettingsPane();

        splitPane.getItems().addAll(mainPane, settingsPane);
        DoubleProperty splitPaneDividerPosition = splitPane.getDividers().get(0).positionProperty();
        splitPaneDividerPosition.set(0.8);
        this.setCenter(splitPane);
    }

    private VBox getSettingsPane() {

        TitledPane algorithmeOption = new TitledPane();
    	algorithmeOption.setText("Nouvel algorithme");
    	GridPane gridAlgorithme = new GridPane();
    	gridAlgorithme.setVgap(4);
    	
    	// Parametre du nombre de ville
    	TextField tfNBVille = new TextField(String.valueOf(NB_VILLE));
    	tfNBVille.setMaxWidth(80);
    	tfNBVille.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle( KeyEvent t ) {
                char ar[] = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                if (!(ch >= '0' && ch <= '9')) {
                	t.consume();
                }
                if(!tfNBVille.getText().isEmpty()) {
                    int tmp = Integer.valueOf(tfNBVille.getText());
                    if(tmp > 0)
                    	NB_VILLE = tmp;
            	}
            }
        });
    	gridAlgorithme.setPadding(new Insets(5, 5, 5, 5));
    	gridAlgorithme.add(new Label("Nombre de ville : "), 0, 0);
    	gridAlgorithme.add(tfNBVille, 1, 0);
    	
    	//Paramètre du nombre de fourmis
    	TextField tfNbFourmis = new TextField(String.valueOf(tmpNbFourmi ));
    	tfNbFourmis.setMaxWidth(80);
    	tfNbFourmis.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle( KeyEvent t ) {
                char ar[] = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                if (!(ch >= '0' && ch <= '9')) {
                	t.consume();
                }
                if(!tfNbFourmis.getText().isEmpty()) {
                    int tmp = Integer.valueOf(tfNbFourmis.getText());
                    if(tmp > 0)
                    	tmpNbFourmi = tmp;
            	}
            }
        });
    	gridAlgorithme.setPadding(new Insets(5, 5, 5, 5));
    	gridAlgorithme.add(new Label("Nombre de Fourmis : "), 0, 1);
    	gridAlgorithme.add(tfNbFourmis, 1, 1);
    	
    	Slider slider = new Slider();
    	slider.setMin(1);
    	slider.setMax(100);
    	slider.setValue(10);
    	gridAlgorithme.setPadding(new Insets(5, 5, 5, 5));
    	gridAlgorithme.add(new Label("Nombre de Fourmis : "), 0, 2);
    	gridAlgorithme.add(slider, 1, 2);
    	
    	slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
            	algorithme.setSpeedFourmi(new_val.intValue());
                   
            }
        });
    	
    	algorithmeOption.setContent(gridAlgorithme);
    	
    	TitledPane plusCourt = new TitledPane();
    	plusCourt.setText("Chemin le plus court");
    	
    	BorderPane panelPlusCourt = new BorderPane();
    	panelPlusCourt.setTop(new Label("Panel plus court"));
    	labelEvalutation = new Label("Evaluation minimal : " + AlgorithmeFourmis.EVAL_MIN);
    	panelPlusCourt.setLeft(labelEvalutation);
    	plusCourt.setContent(panelPlusCourt);

        VBox settingsPane = new VBox(algorithmeOption,plusCourt);
        settingsPane .setMinWidth(200);

        return settingsPane;
    }

    private HBox getMainPane() {
        selectedIndividu = 0;

        createAlgorithme();

        canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        calculerVilles();
        addFourmi();
        drawShapes(gc);

        timer = new MyTimer();

        HBox vbox = new HBox();
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.getChildren().addAll(canvas);
        return vbox;

    }
    private void addFourmi(){
    	NB_FOURMI = tmpNbFourmi;
    	for(int i=0 ; i<NB_FOURMI ; i++) {
            algorithme.ajouterFourmi();
        }
    }

    private void createAlgorithme() {
        algorithme = new AlgorithmeFourmis(NB_VILLE);
        algorithme.randomPheromones();
    }

    public void iterate() {
        timer.start();
    }
    public void reset() {
    	timer.stop();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        createAlgorithme();
        calculerVilles();
        addFourmi();
        drawShapes(gc);
    }
    
    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);

        gc.clearRect(0, 0, 800, 800);
        int offsetX = 280;
        int offsetY = 40;
        if(selectedIndividu == -1) return;

        //Affichage des phéromones
        int taille = algorithme.getTailleCircuit();
        for(int i=0 ; i<taille ; i++) {
            for(int j=i+1 ; j<taille ; j++) {
                Ville v1 = villes.get(i);
                Ville v2 = villes.get(j);
                double intensite = algorithme.getPheromones(i, j) - 1;
                if(intensite < 0) intensite = 0;
                int maxPheromone = algorithme.getMaxPheromone();
                gc.setStroke(new Color(intensite / maxPheromone, 0, 0, intensite / maxPheromone));
                gc.strokeLine(v1.getX()+offsetX, v1.getY()+offsetY, v2.getX()+offsetX, v2.getY()+offsetY);
            }
        }

        ArrayList<Integer> circuit = algorithme.getMeilleurCircuit();
        gc.setStroke(Color.BLUE);
        if(circuit != null) {
            for(int i=0 ; i<NB_VILLE ; i++) {
                Ville v1 = villes.get(circuit.get(i));
                Ville v2;
                if(i + 1 < NB_VILLE) v2 = villes.get(circuit.get(i+1));
                else v2 = villes.get(circuit.get(0));

                gc.strokeLine(v1.getX()+offsetX, v1.getY()+offsetY, v2.getX()+offsetX, v2.getY()+offsetY);
            }
        }

        //On affiche les villes
        for(int i=0 ; i<villes.size() ; i++) {
            Ville v = villes.get(i);
            gc.setFill(v.getColor());
            gc.fillOval(v.getX() -5+offsetX, v.getY() -5 + offsetY, 10, 10);
        }

        gc.setFill(Color.CYAN);

        //On affiche les fourmis
        ArrayList<Fourmi> fourmis = algorithme.getFourmis();
        for(int i=0 ; i<fourmis.size() ; i++) {
            double x = fourmis.get(i).getX() + offsetX;
            double y = fourmis.get(i).getY() + offsetY;

            gc.fillPolygon(new double[]{x-3, x+3, x}, new double[]{y+3, y+3, y-3}, 3);
        }
    }

    private void calculerVilles() {
        //On génère aléatoirement les villes
        villes = new ArrayList<Ville>();

        for(int i=0 ; i<NB_VILLE ; i++) {
            villes.add(new Ville((int)(Math.random() * 490 + 5), (int)(Math.random() * 490 + 5)));
        }

        algorithme.setVilles(villes);
    }

    private class MyTimer extends AnimationTimer {
        @Override
        public void handle(long now) {
            doHandle();
        }

        private void doHandle() {
            algorithme.iterate();
            labelEvalutation.setText("Evaluation minimal : " + AlgorithmeFourmis.EVAL_MIN);
            drawShapes(canvas.getGraphicsContext2D());
        }
    }

	public ArrayList<Ville> save() {
		return algorithme.getVilles();
		// TODO Auto-generated method stub
		
	}

	public void load(ArrayList<Ville> saveArray) {
		NB_VILLE = saveArray.size();
		timer.stop();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        createAlgorithme();
        villes = saveArray;
        algorithme.setVilles(villes);
       // calculerVilles();
        addFourmi();
        drawShapes(gc);
		
	};
}