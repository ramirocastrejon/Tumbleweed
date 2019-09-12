import java.io.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.input.*;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.application.Platform;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
public class Editor extends Application // Needs to extend Application to run as main function
{
	public void start(Stage stage)
	{
		stage.setTitle("Editor");
		Scene scene = new Scene(new VBox(), 800, 600);

		TextArea textArea = new TextArea();

		MenuBar menuBar = new MenuBar();
		final VBox vbox = new VBox();

		Menu menuFile = new Menu("File");
		MenuItem newProjectItem= new MenuItem("New Project");
		newProjectItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
		newProjectItem.setOnAction((ActionEvent t) -> {


			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER_LEFT);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25,25,25,25));

			Scene newProjectWindow = new Scene(grid, 400, 300);
			Stage nStage = new Stage();
			nStage.setTitle("New Project Settings");		

			Label projectName = new Label("Project Name: ");
			TextField projectNameField = new TextField();
			grid.add(projectName, 0, 1);
			grid.add(projectNameField, 1, 1);

			Label projectDir = new Label("Project Directory: ");
			TextField projectDirField = new TextField();
			grid.add(projectDir , 0, 2);
			grid.add(projectDirField, 1, 2);

			final Text errText = new Text();
			grid.add(errText, 1, 6);

			Button confirmBtn = new Button("Create");
			confirmBtn.setOnAction((ActionEvent a) -> {
				try{
					File projectFile = new File(projectDirField.getText() + ".project");
					FileOutputStream fs = new FileOutputStream(projectFile);
					OutputStreamWriter os = new OutputStreamWriter(fs);
					Writer w = new BufferedWriter(os);
					w.write("Project Name: " + projectNameField.getText() + "\n");
					w.write("Project Directory: " + projectDirField.getText() + "\n");
					w.write("Project Libraries: " + "./libs/\n");
					w.close();
					errText.setText("Created project files successfully");
				}catch(IOException e){
					errText.setFill(Color.FIREBRICK);
					errText.setText("Error creating project files...");
				}

			});
			HBox hbBtn = new HBox(10);
			hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn.getChildren().add(confirmBtn);
			grid.add(hbBtn, 1,4);

			Button cancelBtn = new Button("Cancel");
			cancelBtn.setOnAction((ActionEvent a) -> {
				nStage.close();

			});
			HBox hbBtn2 = new HBox(10);
			hbBtn2.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn2.getChildren().add(cancelBtn);
			grid.add(hbBtn2,1,5);
			
			nStage.setScene(newProjectWindow);
			nStage.show();

			//stage.setTitle("New Project");
		});

		MenuItem openProjectItem = new MenuItem("Open Project");
		openProjectItem.setAccelerator(KeyCombination.keyCombination("Ctrl+B"));


		MenuItem saveItem = new MenuItem("Save");
		saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		exitItem.setOnAction( (ActionEvent t) -> { System.exit(0); } );

		menuFile.getItems().addAll(newProjectItem, openProjectItem, saveItem, exitItem);
		menuBar.getMenus().addAll(menuFile);

		((VBox) scene.getRoot()).getChildren().addAll(menuBar, textArea);

		stage.setScene(scene);
		stage.show();
	}

	public static void main(String args[])
	{
		launch(args);
	}

}
