package tk.wosaj.datagenfx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tk.wosaj.datagenfx.Application;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Start implements Application.ChangelogUpdateListener {

    public static Stage newProjectStage;

    static {
        try {
            newProjectStage = new FXMLLoader(Application.class.getResource("fxml/new_project.fxml")).load();
            newProjectStage.setTitle(Application.properties.getProperty("newProject.title"));
            newProjectStage.getIcons().add(
                    new Image(Objects.requireNonNull(Application.class.getResourceAsStream("images/icon.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    Label versionLabel;
    public TextArea StartChangelog;
    @FXML
    ListView<Label> recent;
    @FXML
    TreeView<String> hierarchy;

    public Start() {
        Application.changelogUpdateEvent.addListener(this);
        NewProject.startInstance = this;
    }

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

    public void createNewProject(String name, String package0, File projectFile) {
        newProjectStage.close();
    }

    @Override
    public void onEvent(Application.ChangelogUpdateEvent event) {
        StartChangelog.setText(Application.changelog);
    }
}
