package Controller;

import View.CardView;
import View.ItemController;
import View.MainProgramView;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class Admin extends Application {
    public CardView card;

    public ScrollPane scroll;
    public GridPane gridPane;
    public ImageView selectedImage;
    public Button decrease;
    public Button increase;
    public Label Stock;
    public Button ban;
    public Button unban;

    @FXML
    public void initialize() {
        decrease.setDisable(true);
        increase.setDisable(true);
        ban.setDisable(true);
        unban.setDisable(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setField();
    }

    private void setField(){
        if (card != null){
            Stock.setText("Stock: " + Shop.getStock(card.name));
        }
        int column = 0;
        int row = 0;
        gridPane.getChildren().clear();
        for (int i = 0; i < CardView.cardViews.size(); i++) {
            if (column == 3) {
                column = 0;
                row++;
            }
            AnchorPane pane = null;
            FXMLLoader fxmlLoader = new FXMLLoader();
            try {
                fxmlLoader.setLocation(getClass().getResource("/FXML/Item.fxml"));
                pane = fxmlLoader.load();
                ItemController itemController = fxmlLoader.getController();
                itemController.setImage(CardView.cardViews.get(i).imageView);
                itemController.setCardView(CardView.cardViews.get(i));
                itemController.setItemListener(new ItemListener() {
                    @Override
                    public void onClick(CardView cardView) {
                        setCard(itemController.getCardView());
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }


            gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
            gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
            gridPane.setMaxWidth(Region.USE_PREF_SIZE);

            gridPane.setMinHeight(Region.USE_COMPUTED_SIZE);
            gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            gridPane.setMaxHeight(Region.USE_PREF_SIZE);


            gridPane.add(pane, column++, row);
            if (isBanned(CardView.cardViews.get(i).name)){
                Circle circle = new Circle(50);
                circle.setFill(new ImagePattern(new Image(getClass().getResource("/Images/Lock.png").toExternalForm())));
                circle.toFront();
                gridPane.add(circle, column-1, row);
            }
        }

    }

    private void setCard(CardView cardView) {
        decrease.setDisable(false);
        increase.setDisable(false);
        ban.setDisable(false);
        unban.setDisable(false);
        card = cardView;
        Stock.setText("Stock: " + Shop.getStock(card.name));
        selectedImage.setImage(cardView.imageView.getImage());
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/FXML/Admin.fxml"));
        Scene scene = new Scene(root, 1200, 700);
        stage.setScene(scene);
        stage.show();
    }

    public void mouseEnter(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setCursor(Cursor.HAND);
    }

    public void mouseExited(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setCursor(Cursor.NONE);
    }

    public static boolean isBanned(String name){
        String result = null;
        try {
            LoginController.dataOutputStream.writeUTF("shop isBanned " + name);
            LoginController.dataOutputStream.flush();
            result = LoginController.dataInputStream.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result.equals("true"))
            return true;
        return false;
    }

    public void ban(MouseEvent mouseEvent) {
        try {
            LoginController.dataOutputStream.writeUTF("shop ban " + card.name);
            LoginController.dataOutputStream.flush();
            String result = LoginController.dataInputStream.readUTF();
            JOptionPane.showMessageDialog(null, result);
        } catch (Exception e){
            e.printStackTrace();
        }
        setField();
    }

    public void unban(MouseEvent mouseEvent) {
        try {
            LoginController.dataOutputStream.writeUTF("shop unban " + card.name);
            LoginController.dataOutputStream.flush();
            String result = LoginController.dataInputStream.readUTF();
            JOptionPane.showMessageDialog(null, result);
        } catch (Exception e){
            e.printStackTrace();
        }
        setField();
    }

    public void decrease(MouseEvent mouseEvent) {
        try {
            LoginController.dataOutputStream.writeUTF("shop decrease " + card.name);
            LoginController.dataOutputStream.flush();
            String result = LoginController.dataInputStream.readUTF();
            JOptionPane.showMessageDialog(null, result);
        } catch (Exception e){
            e.printStackTrace();
        }
        setField();
    }

    public void increase(MouseEvent mouseEvent) {
        try {
            LoginController.dataOutputStream.writeUTF("shop increase " + card.name);
            LoginController.dataOutputStream.flush();
            String result = LoginController.dataInputStream.readUTF();
            JOptionPane.showMessageDialog(null, result);
        } catch (Exception e){
            e.printStackTrace();
        }
        setField();
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new Shop().start(MainProgramView.stage);
    }
}
