package Controller;

import Menus.ImportExportMenu;
import View.CardView;
import View.ItemController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class ExportController{
    private Stage stage;
    private Scene scene;
    public static AnchorPane root;
    public GridPane gridPane;
    public ScrollPane scroll;
    public CardView card;
    public ImageView selectedImage;

    @FXML
    public Button exportButton;

    @FXML
    public void initialize() {
        CardView.init();
        exportButton.setDisable(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        int column = 0;
        int row = 0;
        for (int i = 0; i < CardView.cardViews.size(); i++) {
            if (column == 3){
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

            gridPane.add(pane,column++,row);
        }

    }

    private void setCard(CardView cardView) {
        card = cardView;
        selectedImage.setImage(cardView.imageView.getImage());
        exportButton.setDisable(false);
    }

    public void export(ActionEvent event) {
        new ImportExportMenu().exportCard("export card " + card.name);
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/import_menu_view.fxml"));
        root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
