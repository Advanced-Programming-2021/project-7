package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ImportController {
    private Stage stage;
    private Scene scene;
    public static AnchorPane root;

    @FXML
    private Label commandLabel;
    @FXML
    private Label one;
    @FXML
    private Label two;
    @FXML
    private Label three;
    @FXML
    private Label four;
    @FXML
    private Label five;
    @FXML
    private Label six;
    @FXML
    private Label seven;
    @FXML
    private Label eight;

    private FileChooser fileChooser;
    private File file;
    private ArrayList<Label> labels = new ArrayList<>();

    {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV files", "*CSV")
        );
        labels.add(one);
        labels.add(two);
        labels.add(three);
        labels.add(four);
        labels.add(five);
        labels.add(six);
        labels.add(seven);
        labels.add(eight);
    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/import_menu_view.fxml"));
        root = loader.load();
        makeStage(event);
    }

    public void importCard(ActionEvent event) throws IOException {
        file = fileChooser.showOpenDialog(stage);
        BufferedReader csvReader = new BufferedReader(new FileReader(file));
        csvReader.readLine();
        String line = csvReader.readLine();
        String[] data = line.split(",(?=\\S)");
        // date[ 0 ]:name date[ 1 ]:level data[ 2 ]:attribute data[ 3 ]:MonsterType data[ 4 ]:CardType
        // data[ 5 ]: AttackPoint data[ 6 ]:DefencePoint data[ 7 ]:Description data[ 8 ]:price
        Image image = null;
        String searchName = data[ 0 ].replace("of", "Of").replace(" ", "")
                .replace("-", "").replace("\"", "");
        try {
            image = new Image(getClass().
                    getResource("/Images/Cards/" + searchName + ".jpg").toExternalForm());
            Rectangle mainProfile = new Rectangle();
            mainProfile.setWidth(300);
            mainProfile.setHeight(450);
            mainProfile.setY(30);
            mainProfile.setX(60);
            mainProfile.setStroke(Color.BLACK);
            mainProfile.setStrokeWidth(2);
            makeMainProfileEffect(mainProfile);
            mainProfile.setFill(new ImagePattern(image));
            root.getChildren().add(mainProfile);
        } catch (Exception e) {

        }
        try {
            commandLabel.setText("Card name: " + data[ 0 ]);
            one.setText(data[ 1 ]);
            two.setText(data[ 2 ]);
            three.setText(data[ 3 ]);
            four.setText(data[ 4 ]);
            five.setText(data[ 5 ]);
            six.setText(data[ 6 ]);
            seven.setText(data[ 7 ]);
            eight.setText(data[ 8 ]);
            for (Label label : labels) {
                label.getStylesheets().add(getClass().getResource("/CSS/Main.css").toExternalForm());
                label.getStyleClass().add("score-lab");
            }
        } catch (Exception e) {

        }
    }

    public void makeMainProfileEffect(Rectangle rectangle) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.YELLOW);
        dropShadow.setSpread(1);
        dropShadow.setRadius(1);
        dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
        rectangle.setEffect(dropShadow);
    }

    public void makeStage(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
