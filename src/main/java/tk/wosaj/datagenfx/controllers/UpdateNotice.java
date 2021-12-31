package tk.wosaj.datagenfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import tk.wosaj.datagenfx.Application;
import tk.wosaj.datagenfx.version.UpdateManager;

import java.io.IOException;
import java.net.URISyntaxException;

public class UpdateNotice {
    @FXML
    Label Current;
    @FXML
    Label Updated;
    @FXML
    TextArea Changelog;
    @FXML
    Stage updateStage;

    public void initialize() throws URISyntaxException, IOException {
        var bundle = UpdateManager.checkUpdates(Application.properties);
        var data = bundle.data();
        Updated.setText(String.format("New build: %s (%s)",
                data.getStatus().equals("RELEASE") ? data.getVersion() : data.getStatus() + " " + data.getVersion(),
                data.getDate()
                ));

        var splitted = Application.properties.getProperty("version").split(" ");

        Current.setText(String.format("Current build: %s",
                splitted[0].equals("RELEASE") ? splitted[1] : Application.properties.getProperty("version")
        ));

        var changelog = new StringBuilder();
        data.getChangelog().forEach(s -> changelog.append(s).append("\n"));

        Changelog.setText(changelog.toString());
    }

    public void exit() {
        updateStage.close();
    }

    public void download() {
        Application.hostServices.showDocument(Application.properties.getProperty("update.downloadurl"));
    }
}
