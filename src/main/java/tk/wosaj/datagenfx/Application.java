package tk.wosaj.datagenfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Properties;

public class Application extends javafx.application.Application {

    public static Stage stage;
    public static final Properties properties = new Properties();

    static {
        try {
            properties.load(Application.class.getResourceAsStream("start.properties"));
        } catch (IOException e) {
            System.err.println("Properties loading error\n");
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/start.fxml"));
        Scene scene = new Scene(
                fxmlLoader.load(),
                Double.parseDouble(properties.getProperty("main.width")),
                Double.parseDouble(properties.getProperty("main.height")));
        stage.setTitle(properties.getProperty("main.title"));
        stage.setScene(scene);
        stage.getIcons().add(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("icon.png"))));
        stage.show();
        Application.stage = stage;
    }

    public static void main(String[] args) {
        launch();
    }
}