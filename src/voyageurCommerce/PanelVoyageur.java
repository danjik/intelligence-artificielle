package voyageurCommerce;


import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PanelVoyageur extends BorderPane {

    private int NB_VILLE = 20;
    private int NB_INDIVIDU = 10;
    
    private int iterations = 1;
	private ArrayList<Ville> villes;
    private Algorithme algorithme;
    private int selectedIndividu;
    private ListView<Individu> listView;
	private Canvas canvas;
    private HBox mainPane;

	private double tmpProbaMutation = 0.2;
	
	private boolean affichePlusCourt;
	private Label labelEvalutation;

	private int tmpEvalMin = 20;

    public PanelVoyageur(){

        SplitPane splitPane = new SplitPane();

        affichePlusCourt = false;
        mainPane = getMainPane();
        VBox settingsPane = getSettingsPane();
        
        splitPane.getItems().addAll(mainPane, settingsPane);
        DoubleProperty splitPaneDividerPosition = splitPane.getDividers().get(0).positionProperty();
        splitPaneDividerPosition.set(0.8);
        this.setCenter(splitPane);
    }

    private VBox getSettingsPane() {
    	// Parametre durant l'execution de l'algorithme
    	TitledPane parametre = new TitledPane();
    	parametre.setText("Paramètres");
    	GridPane gridParametre = new GridPane();
    	gridParametre.setVgap(4);
    	TextField tfIterations = new TextField("1");
    	tfIterations.setMaxWidth(80);
    	tfIterations.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle( KeyEvent t ) {
                char ar[] = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                if (!(ch >= '0' && ch <= '9')) {
                   t.consume();
                }
                if(!tfIterations.getText().isEmpty()) {
                    int tmp = Integer.valueOf(tfIterations.getText());
                    if(tmp > 0)
                        iterations = tmp;
                }
             }
          });
    	gridParametre.setPadding(new Insets(5, 5, 5, 5));
    	gridParametre.add(new Label("Nombre d'itérations : "), 0, 0);
    	gridParametre.add(tfIterations, 1, 0);
    	parametre.setContent(gridParametre);
    	
    	//Parametres utilisés lors de la création d'un nouvel algorithme
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
    	
    	// Parametre du nombre d'individu
    	TextField tfNBIndividu = new TextField(String.valueOf(NB_INDIVIDU));
    	tfNBIndividu.setMaxWidth(80);
    	tfNBIndividu.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle( KeyEvent t ) {
                char ar[] = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                if (!(ch >= '0' && ch <= '9')) {
                	t.consume();
                }
                if(!tfNBIndividu.getText().isEmpty()) {
                    int tmp = Integer.valueOf(tfNBIndividu.getText());
                    if(tmp > 0)
                    	NB_INDIVIDU = tmp;
            	}
            }
        });
    	gridAlgorithme.setPadding(new Insets(5, 5, 5, 5));
    	gridAlgorithme.add(new Label("Nombre d'individu : "), 0, 1);
    	gridAlgorithme.add(tfNBIndividu, 1, 1);
    	
    	//Paramètre de la probabilité de mutation pour un individu
    	TextField tfProbaMutation = new TextField(String.valueOf(tmpProbaMutation));
    	tfProbaMutation.setMaxWidth(80);    
    	tfProbaMutation.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
            public void handle( KeyEvent t ) {
            	if (tfProbaMutation.getText().matches("[0-1].[0-9]+")) {
                	tmpProbaMutation = Double.valueOf(tfProbaMutation.getText());
                	tfProbaMutation.getStyleClass().clear();
                	tfProbaMutation.getStyleClass().addAll("text-field", "text-input");
                }
                else{
                	tfProbaMutation.getStyleClass().add("tfError");
                }
            }
        });
    	gridAlgorithme.setPadding(new Insets(5, 5, 5, 5));
    	gridAlgorithme.add(new Label("Proba mutations : "), 0, 2);
    	gridAlgorithme.add(tfProbaMutation, 1, 2);
    	
    	// Parametre de l'evaluation minimum qui sert de base pour la roulette russe
    	TextField tfEvalMin = new TextField(String.valueOf(tmpEvalMin));
    	tfEvalMin.setMaxWidth(80);
    	tfEvalMin.addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
            public void handle( KeyEvent t ) {
                char ar[] = t.getCharacter().toCharArray();
                char ch = ar[t.getCharacter().toCharArray().length - 1];
                if (!(ch >= '0' && ch <= '9' && tfEvalMin.getText().length() <= 2)) {
                	t.consume();
                }
                if(!tfEvalMin.getText().isEmpty()) {
                    int tmp = Integer.valueOf(tfEvalMin.getText());
                    if(tmp > 0)
                    	tmpEvalMin = tmp;
            	}
            }
        });
    	gridAlgorithme.setPadding(new Insets(5, 5, 5, 5));
    	gridAlgorithme.add(new Label("Eval Min : "), 0, 3);
    	gridAlgorithme.add(tfEvalMin, 1, 3);
    	
    	
    	algorithmeOption.setContent(gridAlgorithme);
    	
    	TitledPane plusCourt = new TitledPane();
    	plusCourt.setText("Chemin le plus court");
    	
    	BorderPane panelPlusCourt = new BorderPane();
    	panelPlusCourt.setTop(new Label("Panel plus court"));
    	labelEvalutation = new Label("Evaluation minimal : " + algorithme.getPlusCourt());
    	panelPlusCourt.setLeft(labelEvalutation);
    	CheckBox checkbox = new CheckBox("Afficher");
    	checkbox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				affichePlusCourt = newValue;
				drawShapes(canvas.getGraphicsContext2D());
			}
		});
    	panelPlusCourt.setBottom(checkbox);
    	
    	
    	
    	plusCourt.setContent(panelPlusCourt);
        
        VBox settingsPane = new VBox(parametre,algorithmeOption,plusCourt);
		settingsPane .setMinWidth(200);
        
        return settingsPane;
	}

	private HBox getMainPane() {
        selectedIndividu = 0;

        
        createAlgorithme();

        canvas = new Canvas(600, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        calculerVilles();
        drawShapes(gc);

       
        createListView(gc);

        HBox vbox = new HBox();
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.getChildren().addAll(listView,canvas);
        return vbox;

	}
    private void createListView(GraphicsContext gc) {
    	 //Création de la ListView
        ObservableList<Individu> individus = FXCollections.observableArrayList();
        listView = new ListView<Individu>(individus);
        listView.setMinWidth(200);
        listView.setMaxWidth(200);
        listView.setEditable(true);

        updateListView();
        listView.setCellFactory((ListView<Individu> l) -> new ListIndividu());

        //Listeners
        listView.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Individu> ov, Individu old_val,
                 Individu new_val) -> {
                    selectedIndividu = new_val.getNumero();
                    drawShapes(gc);
                });
		
	}

	private void createAlgorithme() {
    	//Création d'un algorithme
        algorithme = new Algorithme(NB_VILLE,tmpProbaMutation,tmpEvalMin);
        for(int i=0 ; i<NB_INDIVIDU ; i++) {
            algorithme.addRandomIndividu();
        }
		
	}

    public void reset() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        createAlgorithme();
        calculerVilles();
        createListView(gc);
        updateListView();
        labelEvalutation.setText("Evaluation minimal " + Algorithme.valMin);
        drawShapes(gc);
    }

    public void iteration() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        algorithme.iteration(iterations);
        algorithme.evaluerIndividus();
        updateListView();
        labelEvalutation.setText("Evaluation minimal " + algorithme.getPlusCourt());
        drawShapes(gc);
    }

	private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);

        gc.clearRect(0, 0, 800, 800);
        int offsetX = 80;
        int offsetY = 40;
        if(selectedIndividu == -1) return;
        Individu test = algorithme.getIndividu(selectedIndividu);
        ArrayList<Integer> circuit;
        if(!affichePlusCourt){
        	circuit = test.getCircuit();
        }
        else{
        	circuit = algorithme.getMeilleurCircuit();
        }
      
        //Affichage d'un circuit
        for(int i=0 ; i<NB_VILLE ; i++) {
            Ville v1 = villes.get(circuit.get(i));
            Ville v2;
            if(i + 1 < NB_VILLE) v2 = villes.get(circuit.get(i+1));
            else v2 = villes.get(circuit.get(0));

            gc.strokeLine(v1.getX()+offsetX, v1.getY()+offsetY, v2.getX()+offsetX, v2.getY()+offsetY);
        }

        //On affiche les villes
        for(int i=0 ; i<villes.size() ; i++) {
            Ville v = villes.get(i);

	        gc.setFill(v.getColor());
			gc.fillOval(v.getX() -5+offsetX, v.getY() -5 + offsetY, 10, 10);
        }
    }

    private void calculerVilles() {
        //On génère aléatoirement les villes
        villes = new ArrayList<Ville>();

        for(int i=0 ; i<NB_VILLE ; i++) {
            villes.add(new Ville((int)(Math.random() * 490 + 5), (int)(Math.random() * 490 + 5)));
        }

        algorithme.setVilles(villes);
        algorithme.evaluerIndividus();
        if(listView != null) updateListView();
    }

    @SuppressWarnings("unchecked")
	private void updateListView() {

        ListView<Individu> liste;

        if(mainPane != null) liste = (ListView<Individu>)mainPane.getChildren().get(0);
        else liste = listView;
        
        ObservableList<Individu> individus = liste.getItems();
        
        algorithme.evaluerIndividus();
        ArrayList<Individu> population = algorithme.getPopulation();

        while(individus.size() > population.size()) {
            individus.remove(individus.size() - 1);
        }

        for(int i=0 ; i<population.size() ; i++) {
            if(i <= individus.size() - 1) individus.set(i, population.get(i));
            else individus.add(population.get(i));
        }

        liste.setItems(individus);
    }

	public ArrayList<Ville> save() {
		return algorithme.getVilles();
		// TODO Auto-generated method stub
		
	}

	public void load(ArrayList<Ville> saveArray) {
		// TODO Auto-generated method stub
		GraphicsContext gc = canvas.getGraphicsContext2D();
		NB_VILLE = saveArray.size();
        createAlgorithme();
        villes = saveArray;
        algorithme.setVilles(villes);
        algorithme.evaluerIndividus();
        if(listView != null) updateListView();
        createListView(gc);
        updateListView();
        labelEvalutation.setText("Evaluation minimal " + Algorithme.valMin);
        drawShapes(gc);
		
	}
}