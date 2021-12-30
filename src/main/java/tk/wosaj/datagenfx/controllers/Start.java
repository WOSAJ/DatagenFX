package tk.wosaj.datagenfx.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tk.wosaj.datagenfx.Application;

import java.io.IOException;
import java.util.Objects;

public class Start {

    public static Stage newProjectStage;

    static {
        try {
            newProjectStage = new FXMLLoader(Application.class.getResource("fxml/new_project.fxml")).load();
            newProjectStage.setTitle(Application.properties.getProperty("newProject.title"));
            newProjectStage.getIcons().add(
                    new Image(Objects.requireNonNull(Application.class.getResourceAsStream("icon.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Label versionLabel;
    public TextArea StartChangelog;
    public ListView recent;

    public void initialize() {
        versionLabel.setText(Application.properties.getProperty("version"));
    }

    public void newProject() {
        if(!newProjectStage.isShowing()) {
            newProjectStage.show();
        }
    }

    public void openProject() {
        var file = new FileChooser().showOpenDialog(Application.stage);
    }

    public void exit() {
        Application.stage.close();
    }
}
