package tk.wosaj.datagenfx.controllers;

import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class NewProject {
    public TextField name;
    public TextField folder;
    public TextField packageEntry;
    public Stage NewProjectStage;

    public void exit() {
        NewProjectStage.close();
    }

    public void browseFolder() {
        var chooser = new DirectoryChooser();
        folder.setText(chooser.showDialog(NewProjectStage).getAbsolutePath());
    }
}
