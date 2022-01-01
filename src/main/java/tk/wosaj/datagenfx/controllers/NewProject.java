package tk.wosaj.datagenfx.controllers;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

import static tk.wosaj.datagenfx.Application.properties;

public class NewProject {

    static Start startInstance;

    public TextField name;
    public TextField folder;
    public TextField packageEntry;
    public Stage NewProjectStage;

    public static final Set<Character> allowedChars =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_$".chars()
                    .mapToObj(c -> (char)c).collect(Collectors.toSet());
    public Label nameErr;
    public Label folderErr;
    public Label packageErr;

    public void exit() {
        NewProjectStage.close();
    }

    public void browseFolder() {
        var chooser = new DirectoryChooser();
        folder.setText(chooser.showDialog(NewProjectStage).getAbsolutePath());
    }

    public void createProject() {
        var namingErrors = checkNamingErrors(name.getText(), false);
        try {
            switch (namingErrors) {
                case EMPTY -> {
                    nameErr.setText(properties.getProperty("newProject.errs.empty"));
                    nameErr.setVisible(true);
                    throw new Exception();
                }
                case BAD_LETTERS -> {
                    nameErr.setText(properties.getProperty("newProject.errs.invalidCharacters"));
                    nameErr.setVisible(true);
                    throw new Exception();
                }
                case NUMBER_STARTING -> {
                    nameErr.setText(properties.getProperty("newProject.errs.numberStarting"));
                    nameErr.setVisible(true);
                    throw new Exception();
                }
                case NON_ASCII -> {
                    nameErr.setText(properties.getProperty("newProject.errs.nonASCII"));
                    nameErr.setVisible(true);
                    throw new Exception();
                }
                default -> nameErr.setVisible(false);
            }
            if(checkNamingErrors(folder.getText(), false) == NameErrorType.EMPTY) {
                folderErr.setText(properties.getProperty("newProject.errs.empty"));
                folderErr.setVisible(true);
                throw new Exception();
            } else {
                    var dir = new File(folder.getText());
                    if(!dir.exists()) {
                        folderErr.setText(properties.getProperty("newProject.errs.notFound"));
                        folderErr.setVisible(true);
                        throw new Exception();
                    } else if(!dir.isDirectory()) {
                        folderErr.setText(properties.getProperty("newProject.errs.notDirectory"));
                        folderErr.setVisible(true);
                        throw new Exception();
                    } else folderErr.setVisible(false);
            }
            switch (checkNamingErrors(packageEntry.getText(), true)) {
                case EMPTY -> {
                    packageErr.setText(properties.getProperty("newProject.errs.empty"));
                    packageErr.setVisible(true);
                    throw new Exception();
                }
                case BAD_LETTERS -> {
                    packageErr.setText(properties.getProperty("newProject.errs.invalidCharacters"));
                    packageErr.setVisible(true);
                    throw new Exception();
                }
                case NUMBER_STARTING -> {
                    packageErr.setText(properties.getProperty("newProject.errs.numberStarting"));
                    packageErr.setVisible(true);
                    throw new Exception();
                }
                case NON_ASCII -> {
                    packageErr.setText(properties.getProperty("newProject.errs.nonASCII"));
                    packageErr.setVisible(true);
                    throw new Exception();
                }
                default -> packageErr.setVisible(false);
            }
            prepareGenerateProject(name.getText(), new File(folder.getText()), packageEntry.getText());
        } catch (Exception ignored) {}
    }

    public static NameErrorType checkNamingErrors(String input, boolean allowPoints) {
        if(input.length() == 0) return NameErrorType.EMPTY;
        var intChars = input.chars().toArray();
        var chars = new char[intChars.length];
        for (int i = 0; i < intChars.length; i++) {
            chars[i] = (char) intChars[i];
        }
        if(chars[0] == '0' ||
           chars[0] == '1' ||
           chars[0] == '2' ||
           chars[0] == '3' ||
           chars[0] == '4' ||
           chars[0] == '5' ||
           chars[0] == '6' ||
           chars[0] == '7' ||
           chars[0] == '8' ||
           chars[0] == '9' ||
           (allowPoints && chars[0] == '.')) return NameErrorType.NUMBER_STARTING;
        if(!StandardCharsets.US_ASCII.newEncoder().canEncode(input)) return NameErrorType.NON_ASCII;
        for (char aChar : chars) {
            if(!(allowedChars.contains(aChar) || (allowPoints && aChar == '.'))) return NameErrorType.BAD_LETTERS;
        }
        return NameErrorType.OK;
    }

    protected void prepareGenerateProject(String name, File directory, String package0) {
        var file = new File(
                (directory.getAbsolutePath().endsWith("/") || directory.getAbsolutePath().endsWith("\\")
                ? directory.getAbsolutePath() +
                        properties.getProperty("file.base.defaultName") +
                        "." + properties.getProperty("file.base.extension")
                : "/" + directory.getAbsolutePath() +
                properties.getProperty("file.base.defaultName") +
                "." + properties.getProperty("file.base.extension")));
        if(file.exists()) {
            new WarningBuilder(
                    "File already exists",
                    "A file with the same name already exists in this location, replace it?")
                    .setOkAction(() -> startInstance.createNewProject(name, package0, file)).show();
        } else {
            try {
                System.out.println(file.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            startInstance.createNewProject(name, package0, file);
        }
    }
}
