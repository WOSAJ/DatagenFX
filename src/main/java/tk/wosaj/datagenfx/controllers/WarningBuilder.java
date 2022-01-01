package tk.wosaj.datagenfx.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tk.wosaj.datagenfx.Application;

import java.io.IOException;

@SuppressWarnings("unused")
public class WarningBuilder {
    private final Warning warning;

    public WarningBuilder() {
        var loader = new FXMLLoader(Application.class.getResource("fxml/warning.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            warning = loader.getController();
        }
        warning.cancelButton.setOnMouseClicked(event -> warning.stage.close());
    }

    public WarningBuilder(String name, String content) {
        this();
        warning.name.setText(name);
        warning.text.setText(content);
    }

    public Warning getWarning() {
        return warning;
    }

    public Stage getStage() {
        return warning.stage;
    }

    public ButtonBar getButtonBar() {
        return warning.buttonBar;
    }

    public Button getCancelButton() {
        return warning.cancelButton;
    }

    public Button getOkButton() {
        return warning.okButton;
    }

    public String getName() {
        return warning.name.getText();
    }

    public String getContent() {
        return warning.text.getText();
    }

    public WarningBuilder setName(Label name) {
        this.warning.name = name;
        return this;
    }

    public WarningBuilder setContent(String content) {
        this.warning.text.setText(content);
        return this;
    }

    public WarningBuilder setOkAction(Runnable action) {
        warning.okButton.setOnMouseClicked(event -> {
            action.run();
            warning.stage.close();
        });
        return this;
    }

    public WarningBuilder setCancelAction(EventHandler<? super MouseEvent> action) {
        warning.cancelButton.setOnMouseClicked(action);
        return this;
    }

    public WarningBuilder addButton(Button button) {
        warning.buttonBar.getButtons().add(button);
        return this;
    }

    public WarningBuilder addButton(String text, EventHandler<? super MouseEvent> action) {
        var button = new Button(text);
        button.setOnMouseClicked(action);
        addButton(button);
        return this;
    }

    public void show() {
        warning.stage.show();
    }
}
