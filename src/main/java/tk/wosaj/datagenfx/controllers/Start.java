package tk.wosaj.datagenfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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

    @FXML
    Label versionLabel;
    @FXML
    TextArea StartChangelog;
    @FXML
    ListView<Label> recent;
    @FXML
    TreeView<String> hierarchy;

    public void initialize() {
        var splitted = Application.properties.getProperty("version").split(" ");
        versionLabel.setText(
                splitted[0].equals("RELEASE") ? splitted[1] : Application.properties.getProperty("version"));
    }

    public void newProject() {
        if(!newProjectStage.isShowing()) {
            newProjectStage.show();
        }
    }

    public void openProject() {
    }

    public void exit() {
        Application.stage.close();
    }
}
