package editor;

import java.io.*;
import java.nio.file.*;
import java.net.URL;

import java.time.Duration;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Collection;
import java.util.Collections;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.Parent;

import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

public class Controller implements Initializable {
    /**
     * At startup, check for valid project dirs in the current working dir.
     * If no project dirs (dirs with a .tumbleweed file in them) are found,
     * prompt user to either create a new project, or optionally, import
     * a project (copy a valid project dir to current working dir).
     */

    private Stage stage;

    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    // Text editor node.
    private final CodeArea editorArea = new CodeArea();

    // Project selection node.
    private final ListView<String> projectListView = new ListView<String>();

    // Default path for storing user projects.
    private String projectsDirectoryPath;

    // Path to the currently opened project.
    private String currentProjectPath;

    // Path to the currently opened file.
    private String currentFilePath;

    // Project files node.
    private TreeView projectTreeView;

    /**
     * bpRoot node can be referenced anywhere in controller after it is loaded (at startup).
     * Main use case being something like:
     *     bpRoot.setLeft(<TreeView node>).
     *     bpRoot.setCenter(<CodeArea node>).
     */
    @FXML
    private BorderPane bpRoot;

    public void init(Stage primaryStage) {
        this.stage = primaryStage;
    }

    private ArrayList<String> getProjectListing(String path) {
        ArrayList<String> projects = new ArrayList<String>();
        File dir = new File(path);
        for (File dirItem: dir.listFiles()) {
            if (dirItem.isDirectory()) {
                File projectFile = new File(dirItem + "/.tumbleweed");
                System.out.println(projectFile);
                if (projectFile.exists()) {
                    projects.add(dirItem.toString());
                }
            }
        };
        return projects;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        projectsDirectoryPath = new String(System.getProperty("user.home") + "/TumbleweedProjects");
        File projectsDirectoryPathFile = new File(projectsDirectoryPath);
        if (!projectsDirectoryPathFile.exists()) {
            System.out.println("Project directory not found. Creating...");
            projectsDirectoryPathFile.mkdir();
            System.out.println("Projects directory created at " + projectsDirectoryPath);
        }
        fileChooser.setInitialDirectory(projectsDirectoryPathFile);
        directoryChooser.setInitialDirectory(projectsDirectoryPathFile);
    }

    private void buildProjectTree(File file, TreeItem<String> parent) {
        // Walk directory.
        if (file.isDirectory()) {
            TreeItem<String> treeItem = new TreeItem<>(file.getName());
            parent.getChildren().add(treeItem);
            for (File f: file.listFiles()) {
                // Recurse.
                buildProjectTree(f, treeItem);
            }
        }
        else {
            parent.getChildren().add(new TreeItem<>(file.getName()));
        }
    }

    private void buildProjectTreeView(String projectPath) {
        // bpRoot left.

        System.out.println("Building tree view for project: " + projectPath);

        TreeItem<String> rootItem = new TreeItem<String>("Project Files");
        rootItem.setExpanded(true);

        File projectDir = new File(projectPath);

        buildProjectTree(projectDir, rootItem);

        TreeView<String> treeView = new TreeView<String>();
        treeView.setShowRoot(false);
        treeView.setRoot(rootItem);

        projectTreeView = treeView;
    }

    private void setProjectDetailView() {
        // bpRoot right.
    }

    private void setProjectListView() {
        // bpRoot center.
    }

    public void setupUI() {
        /**
         * Setup UI after primary window is loaded. 
         * Check if there are any project dirs, if not, show NewProjectDialog.
         * - Set currentProjectPath to newly created/imported project.
         * - Show editorView.
         * Otherwise, check if currentProjectPath is null.
         * - If null, show projectListView (BorderPane with menubar, project listing, and status bar (top, center, bottom)).
         * - If currentProjectPath is not null... eventually add support for recently/last opened project(s).
         */

        editorArea.setParagraphGraphicFactory(LineNumberFactory.get(editorArea));
        Subscription cleanupWhenNoLongerNeedIt = editorArea.multiPlainChanges()
            .successionEnds(Duration.ofMillis(250))
            .subscribe(ignore -> {
                editorArea.setStyleSpans(0, SyntaxHighlighter.highlight(editorArea.getText()));
            });

        if (bpRoot instanceof BorderPane) {
            System.out.println("bpRoot loaded and is of type BorderPane. Proceeding...");
        }
        else {
            System.out.println("Could not load bpRoot or bpRoot is not of type BorderPane. Exiting...");
            System.exit(0);
        }

        // Get all project directories.
        ArrayList<String> projectListing = getProjectListing(projectsDirectoryPath);

        // Populate projectListView.
        for (String project: projectListing) {
            projectListView.getItems().add(project);
        }

        // Open project on mouse click.
        projectListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String selectedProject = projectListView.getSelectionModel().getSelectedItem().toString();
                System.out.println(selectedProject + " selected.");

                // Set currentProjectPath.
                System.out.println("Setting project path...");
                currentProjectPath = selectedProject;
                System.out.println("Current Project Path: " + currentProjectPath);

                // Show editor view.
                // - Border Pane left node is set to projectTreeView.
                // - Border Pane center is set to null (empty area, can change to empty editorView).
                // - Border Pane right is set to projectDetailView (not yet implemented).
                // - Border Pane top is the same.
                // - Border Pane bottom is the same.

                stage.setTitle(new String("Project: " + currentProjectPath));
                buildProjectTreeView(currentProjectPath);
                bpRoot.setLeft(projectTreeView);
                bpRoot.setCenter(null);
                // bpRoot.setRight(projectDetailView); // Not yet implemented.
            }
        });

        // If there aren't any projects, prompt user to create a new project.
        if (projectListing.size() == 0) {
            newProject();
        }
        else {
            stage.setTitle("Select Project");
            bpRoot.setCenter(projectListView);
        }
    }

    private void createProject(String projectDir) {
        try {
            if (projectDir != null) {
                System.out.println("Creating project " + projectDir + "...");

                new File(projectDir + "/lib").mkdirs(); // Returns a boolean.

                File projFile = new File(projectDir + "/.tumbleweed");
                projFile.createNewFile();

                File mainClassFile = new File(projectDir + "/Main.java");
                mainClassFile.createNewFile();

                // Can setup some basic code here in the file, i.e. public class Main, etc.
                // FileOutputStream fs = new FileOutputStream(mainClassFile);
                // fs.close();

                System.out.println("Project created successfully.");
            } else {
                throw new FileNotFoundException("File not found.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newProject() {
        String projectDirectory = NewProjectDialog.display("Create Project");

        if (projectDirectory != null && projectDirectory.length() > 0) {
            createProject(projectsDirectoryPath + "/" + projectDirectory);
            currentProjectPath = projectsDirectoryPath + "/" + projectDirectory;

            // Add newly created project to projectListView.
            projectListView.getItems().add(currentProjectPath);

            stage.setTitle("Project: " + currentProjectPath);

            buildProjectTreeView(currentProjectPath);

            bpRoot.setLeft(projectTreeView);
            bpRoot.setCenter(null);
            // bpRoot.setCenter(editorArea);
        }
    }

    public void openProject() {
        try {
            directoryChooser.setTitle("Open Project");
            File dir = directoryChooser.showDialog(stage);
            if (dir != null) {
                String projectDir = dir.toString();

                currentProjectPath = projectDir;

                stage.setTitle("Project: " + projectDir);

                if (currentFilePath != null) {
                    currentFilePath = null;
                }

                if (bpRoot.centerProperty() != null) {
                    bpRoot.setCenter(null);
                }

                if (bpRoot.leftProperty() != null) {
                    bpRoot.setLeft(null);
                }

                buildProjectTreeView(currentProjectPath);
                bpRoot.setLeft(projectTreeView);

                if (editorArea != null) {
                    editorArea.clear();
                }

                // bpRoot.setCenter(editorArea);
            } else {
                throw new FileNotFoundException("File not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProject() {
        // Write all project files to disk.
        System.out.println("Saving project...");
    }

    public void closeProject() {
        // Close project.

        System.out.println("Closing project...");

        editorArea.clear();

        bpRoot.setLeft(null);
        bpRoot.setCenter(projectListView);

        currentProjectPath = null;
        currentFilePath = null;

        stage.setTitle("Select Project");
    }

    public void importProject() {
        // Eventually...
        System.out.println("Importing project...");
    }

    public void newFile() {
        if (currentProjectPath != null) {
            String newFileName = NewFileDialog.display("Create File");
            if (newFileName != null && newFileName.length() > 0) {
                String newFilePath = currentProjectPath + "/" + newFileName;
                try {
                    new File(newFilePath).createNewFile();
                    currentFilePath = newFilePath;
                    bpRoot.setCenter(editorArea);
                    editorArea.clear();
                    buildProjectTreeView(currentProjectPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            System.out.println("Not in an open project. Cannot create a new file.");
        }
    }

    public void openFile() {
        if (currentProjectPath != null) {
            try {
                fileChooser.setTitle("Open File");
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    currentFilePath = file.toString();
                    System.out.println("Current File: " + currentFilePath);
                } else {
                    throw new FileNotFoundException("File not found.");
                }
                bpRoot.setCenter(editorArea);
                editorArea.clear();
                String text = new String(Files.readAllBytes(Paths.get(currentFilePath)));
                editorArea.replaceText(text);
                editorArea.setStyleSpans(0, SyntaxHighlighter.highlight(editorArea.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeToFile(String str, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] data = str.getBytes();
            fileOutputStream.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveFile() {
        if (currentFilePath != null && editorArea != null) {
            String text = editorArea.getText();
            File currentFile = new File(currentFilePath);
            System.out.println("Saving file...");
            writeToFile(text, currentFile);
            System.out.println("File saved.");
        }
    }

    public void closeFile() {
        if (currentProjectPath != null) {
            System.out.println("Closing file...");

            editorArea.clear();

            // Close editorArea.
            bpRoot.setCenter(null);

            currentFilePath = null;
        }
    }

    public void selectAll() {
        // Eventually...
        System.out.println("Selecting all text...");
    }

    public void exit() {
        // Exit the program.
        System.out.println("Exiting...");
        System.exit(0);
    }
}
