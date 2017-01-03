package essai;

import java.util.ArrayList;

import colonieFourmis.PanelFourmis;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import voyageurCommerce.PanelVoyageur;
import voyageurCommerce.Ville;

public class Main  extends Application{
    public static void main(String[] args) {
        launch(args);
    }

	private PanelVoyageur panelVoyageur;
	private PanelFourmis panelFourmis;
	private BorderPane root;
	private ArrayList<Ville> saveVille;

	@Override
	public void start(Stage primaryStage) throws Exception {
        MenuBar menu = getMenu();
        panelVoyageur = new PanelVoyageur();
        panelFourmis = new PanelFourmis();
        FlowPane paneCopyright = new FlowPane(new Label("Aragon Nicolas & Taillefer Jordan \u00a9 Faculté des sciences"));
        paneCopyright.setAlignment(Pos.TOP_CENTER);
        root = new BorderPane(panelVoyageur,menu, null, paneCopyright, null);
        Scene scene = new Scene(root, 1100, 750);
        scene.getStylesheets().add("/resources/css/style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	public void setPanelCenter(BorderPane newPane){
		root.setCenter(newPane);
	}

	public MenuBar getMenu(){
    	 //Menu
        MenuBar menu = new MenuBar();

        Menu menuVoyageur = new Menu("Génétique");
        Menu menuFourmi = new Menu("Fourmi");

        // Menu Voyageur de commerce
        MenuItem mItemVoyageur = new MenuItem("Lancer génétique");
        MenuItem mItemVoyageurReset = new MenuItem("Reset");
        MenuItem mItemVoyageurIterate = new MenuItem("Itération");
        MenuItem mItemVoyageurSave = new MenuItem("Sauvegarder");
        MenuItem mItemVoyageurLoad = new MenuItem("Charger");
        
        MenuItem mItemFourmi = new MenuItem("Lancer Fourmi");
        MenuItem mItemFourmiReset = new MenuItem("Reset");
        MenuItem mItemFourmiIterate = new MenuItem("Itération");
        MenuItem mItemFourmiSave = new MenuItem("Sauvegarder");
        MenuItem mItemFourmiLoad = new MenuItem("Charger");

        mItemVoyageur.setDisable(true);
    	
    	mItemFourmiReset.setDisable(true);
    	mItemFourmiIterate.setDisable(true);
    	mItemFourmiSave.setDisable(true);
    	mItemFourmiLoad.setDisable(true);
    	mItemVoyageurLoad.setDisable(true);
        
        mItemVoyageur.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	mItemVoyageur.setDisable(true);
            	mItemVoyageurReset.setDisable(false);
            	mItemVoyageurIterate.setDisable(false);
            	mItemVoyageurSave.setDisable(false);
            	

            	mItemFourmi.setDisable(false);
            	mItemFourmiReset.setDisable(true);
            	mItemFourmiIterate.setDisable(true);
            	mItemFourmiSave.setDisable(true);
            	root.setCenter(panelVoyageur);
            	
            }
        });
        
        mItemFourmi.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	mItemVoyageur.setDisable(false);
            	mItemVoyageurReset.setDisable(true);
            	mItemVoyageurIterate.setDisable(true);
            	mItemVoyageurSave.setDisable(true);

            	mItemFourmi.setDisable(true);
            	mItemFourmiReset.setDisable(false);
            	mItemFourmiIterate.setDisable(false);
            	mItemFourmiSave.setDisable(false);
            	root.setCenter(panelFourmis);
            	
            }
        });
        mItemVoyageurReset.setAccelerator(KeyCombination.keyCombination("Shift+R"));
        mItemVoyageurReset.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                panelVoyageur.reset();
            }
        });
        mItemFourmiReset.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        mItemFourmiReset.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	panelFourmis.reset();
            }
        });
        
        mItemVoyageurIterate.setAccelerator(KeyCombination.keyCombination("Shift+I"));
        mItemVoyageurIterate.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                panelVoyageur.iteration();
            }
        });

        mItemFourmiIterate.setAccelerator(KeyCombination.keyCombination("Ctrl+I"));
        mItemFourmiIterate.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                panelFourmis.iterate();
            }
        });
        mItemFourmiSave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        mItemFourmiSave.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	saveVille = panelFourmis.save();
            	mItemFourmiLoad.setDisable(false);
            	mItemVoyageurLoad.setDisable(false);
            	
            }
        });
        mItemVoyageurSave.setAccelerator(KeyCombination.keyCombination("Shift+S"));
        mItemVoyageurSave.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	saveVille = panelVoyageur.save();
            	mItemFourmiLoad.setDisable(false);
            	mItemVoyageurLoad.setDisable(false);
            }
        });
        mItemFourmiLoad.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
        mItemFourmiLoad.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	panelFourmis.load(saveVille);
            }
        });
        mItemVoyageurLoad.setAccelerator(KeyCombination.keyCombination("Shift+L"));
        mItemVoyageurLoad.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	panelVoyageur.load(saveVille);
            }
        });
        
        
        //Quitter
        MenuItem quitter = new MenuItem("Quitter");
        quitter.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });
        menuVoyageur.getItems().addAll(mItemVoyageur, new SeparatorMenuItem(),mItemVoyageurReset, mItemVoyageurIterate, mItemVoyageurSave,mItemVoyageurLoad );
        menuFourmi.getItems().addAll(mItemFourmi, new SeparatorMenuItem(),mItemFourmiReset, mItemFourmiIterate, mItemFourmiSave , mItemFourmiLoad);

        menu.getMenus().addAll(menuVoyageur,menuFourmi);
        return menu;
    }

}
