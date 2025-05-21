package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Controller controller = new Controller();
            Scene scene = new Scene(controller.getView(), 600, 600);
            
            boolean cssLoaded = false;
            
            URL cssResource = getClass().getResource("style.css");
            if (cssResource != null) {
                scene.getStylesheets().add(cssResource.toExternalForm());
                cssLoaded = true;
                System.out.println("CSS loaded via direct resource");
            }
            
            if (!cssLoaded) {
                File srcCssFile = new File("src/gui/style.css");
                if (srcCssFile.exists()) {
                    scene.getStylesheets().add(srcCssFile.toURI().toURL().toExternalForm());
                    cssLoaded = true;
                    System.out.println("CSS loaded from src directory");
                }
            }
            
            if (!cssLoaded) {
                File binCssFile = new File("bin/gui/style.css");
                if (binCssFile.exists()) {
                    scene.getStylesheets().add(binCssFile.toURI().toURL().toExternalForm());
                    cssLoaded = true;
                    System.out.println("CSS loaded from bin directory");
                }
            }
            
            if (!cssLoaded) {
                System.out.println("Could not load external CSS, using inline styles");
                String css = 
                    ".root { -fx-font-family: \"Segoe UI\", sans-serif; -fx-background-color: #f8f9fa; }" +
                    ".header-label { -fx-font-size: 36px; -fx-font-weight: extrabold; -fx-text-fill: #2c3e50; -fx-padding: 20 0 10 0; }" +
                    ".combo-box { -fx-pref-width: 200; -fx-font-size: 14px; }" +
                    ".button { -fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16; }";
                
                scene.getStylesheets().add("data:text/css," + css);
            }
            
            primaryStage.setTitle("Rush Hour PathFinder");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}