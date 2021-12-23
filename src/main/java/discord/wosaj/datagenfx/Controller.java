package discord.wosaj.datagenfx;

import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;

public class Controller {
    public MenuBar menu_bar;
    public Label some_label;
    public TabPane main_tab;

    public void initialize() {
    }

    public void swapDisable() {
        some_label.setText(some_label.isDisable() ? "Enabled" : "Disabled");
        some_label.setDisable(!some_label.isDisable());
    }

    public void exit() {
        Application.stage.close();
    }
}