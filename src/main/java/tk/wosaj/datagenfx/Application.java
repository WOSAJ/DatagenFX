package tk.wosaj.datagenfx;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import tk.wosaj.datagenfx.controllers.Start;
import tk.wosaj.datagenfx.version.UpdateManager;

import java.io.*;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;

public class Application extends javafx.application.Application {

    public static final ChangelogUpdateEvent changelogUpdateEvent = new ChangelogUpdateEvent();
    public static Stage stage;
    public static HostServices hostServices;
    public static String changelog = "";
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
    public void start(Stage stage) throws IOException {
        hostServices = getHostServices();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/start.fxml"));
        Scene scene = new Scene(
                fxmlLoader.load(),
                Double.parseDouble(properties.getProperty("main.width")),
                Double.parseDouble(properties.getProperty("main.height")));
        stage.setTitle(properties.getProperty("main.title"));
        stage.setScene(scene);
        stage.getIcons().add(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/icon.png"))));
        var updateThread = new Thread(Application::noticeUpdate);
        updateThread.start();
        stage.setOnCloseRequest(e -> Platform.exit());
        Application.stage = stage;
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private static void noticeUpdate() {
        Platform.runLater(() -> {
            try {
                UpdateManager.UpdateBundle bundle = UpdateManager.checkUpdates(properties);
                var logBuilder = new StringBuilder();
                for (String entry : bundle.data().getChangelog()) {
                    logBuilder.append(entry).append("\n");
                }
                changelog = logBuilder.toString();
                changelogUpdateEvent.fire();
                System.out.println(bundle.status());
                if(bundle.status() == UpdateManager.UpdateStatus.NEW_VERSION_AVAILABLE) {
                    var fxmlLoader = new FXMLLoader(Application.class.getResource("fxml/update.fxml"));
                    var stage = (Stage) fxmlLoader.load();
                    stage.getIcons().add(
                            new Image(Objects.requireNonNull(
                                    Application.class.getResourceAsStream("images/icon.png"))));
                    stage.show();
                }
            } catch (UnknownHostException e) {
                System.err.println("Unable to connect to address " +
                        Application.properties.getProperty("update.jsonurl"));
            } catch (URISyntaxException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("unused")
    public static class ChangelogUpdateEvent {
        private final List<ChangelogUpdateListener> listeners = new ArrayList<>();

        public void addListener(ChangelogUpdateListener listener) {
            listeners.add(listener);
        }

        public List<ChangelogUpdateListener> getListeners() {
            return listeners;
        }

        public void removeListener(ChangelogUpdateListener listener) {
            listeners.remove(listener);
        }

        protected void fire() {
            listeners.forEach(listener -> listener.onEvent(this));
        }
    }

    public interface ChangelogUpdateListener extends EventListener {
        void onEvent(ChangelogUpdateEvent event);
    }
}