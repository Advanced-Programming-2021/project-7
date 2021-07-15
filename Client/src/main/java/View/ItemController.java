package View;

import Controller.ItemListener;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.awt.event.MouseEvent;

public class ItemController {

    private CardView cardView;
    private ItemListener itemListener;

    @FXML
    private ImageView image = new ImageView();

    public void setImage(ImageView image) {
        this.image.setImage(image.getImage());
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    public CardView getCardView() {
        return cardView;
    }

    public ImageView getImage() {
        return image;
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        itemListener.onClick(cardView);
    }
}
