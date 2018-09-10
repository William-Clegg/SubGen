package com.company;

import Windows.ProjectInfoWindow;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.*;
import static Windows.OutlineWindow.outlineGrid;

public class SubGenApp extends Application {

    public static Stage window;
    public static Scene scene, scene1;
    public final static ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();

    public static ObservableList<String> subSheets = FXCollections.observableArrayList();

    public static List<String> contentList = new ArrayList<>();

    public static TreeItem<String> root;
    public static TreeView<String> treeView;

    public static List<String> southernList = new ArrayList<>();
    public static List<Integer> southernListIndex = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        FileInputStream iconStream = new FileInputStream("C:\\Users\\Rudy\\IdeaProjects\\SubGen\\src\\SGblackLarge.png");
        window.getIcons().add(new Image(iconStream));
        window.setTitle("Submittal Generator");

        GridPane grid = ProjectInfoWindow.createGrid();

        root = new TreeItem<>("Submittal");
        root.setExpanded(true);
        treeView = new TreeView<String>(root);
        treeView.setCellFactory(param -> new CustomTreeCell());

        scene = new Scene(grid, 900, 700);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public static void createScene1() {

        GridPane grid = outlineGrid();
        window.setScene(scene1);
    }

    public static void main(String[] args) {
        launch(args);
    }}







