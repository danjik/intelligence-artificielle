package voyageurCommerce;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Created by Nicolas on 26/02/2016.
 */
public class ListIndividu extends ListCell<Individu> {
	
	@Override
    public void updateItem(Individu item, boolean empty) {
        super.updateItem(item, empty);
        float diffMaxMin = Algorithme.valMax - Algorithme.valMin;
        

        Pane pane = null;

        if(item == null) {
            setText(null);
            setGraphic(null);
        }
        else {
            pane = new Pane();
            
            final Text leftText = new Text("Individu " + Integer.toString(item.getNumero()));
            
            final double em = leftText.getLayoutBounds().getHeight();
            leftText.relocate(0, 0);
            final Text rightText = new Text(Integer.toString(item.getEvaluation()));
            float val = Algorithme.valMax - item.getEvaluation();
            float resC = val / diffMaxMin;
            rightText.setFill(new Color(1 - resC,resC,0.0,1.0));
            final double width = rightText.getLayoutBounds().getWidth();
            rightText.relocate(8 * em - width, 0);

            pane.getChildren().addAll(leftText, rightText);

            setText("");
            setGraphic(pane);
        }
    }
}
