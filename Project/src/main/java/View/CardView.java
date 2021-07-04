package View;

import Controller.ItemListener;
import Model.Cards.Card;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class CardView implements Comparable<CardView> {
    public static ArrayList<CardView> cardViews = new ArrayList<>();
    @FXML
    public ImageView imageView;

    @FXML
    public String name;

    private ItemListener itemListener;

    public CardView(String name){
        this.name = name;
        imageView = new ImageView();
        if (name.equals("\"Terratiger, the Empowered Warrior\""))
            name = "Terratiger";
        String searchName = name.replace("of", "Of").replace(" ", "").replace("-", "");
        String url = "/Images/Cards/" + searchName + ".jpg";
        Image image = new Image(getClass().getResource(url).toExternalForm());
        imageView.setImage(image);
        cardViews.add(this);
    }

    public static void init(){
        if (cardViews.size() != 0)
            return;
        for (Card card : Card.getCards()) {
            new CardView(card.getName());
        }
        Collections.sort(cardViews);
    }

    @Override
    public int compareTo(CardView o) {
        return this.name.compareTo(o.name);
    }

    public static CardView getCardViewByName(String name){
        for (int i = 0; i < cardViews.size(); i++) {
            if (cardViews.get(i).name.equals(name))
                return cardViews.get(i);
        }
        return null;
    }

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }
}
