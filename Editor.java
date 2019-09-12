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
import javafx.stage.FileChooser;

public class Editor extends Application // Needs to extend Application to run as main function
{
	//stage is the root window
	public void start(Stage stage)
	{
		stage.setTitle("Editor");
		Scene scene = new Scene(new VBox(), 800, 600); //set the size of the display
		
		//editor box may change to the HTML Editor in the javafx library
		TextArea textArea = new TextArea();
		
		//creates the menubar
		MenuBar menuBar = new MenuBar();
		//creates a menu option in the menubar
		Menu menuFile = new Menu("File");

		//dropdown options for a menu	
		MenuItem newProjectItem= new MenuItem("New Project");
		newProjectItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N")); //set shortcut
		
		//lambda function that handles what happens when this button is clicked or shortcut is used
		newProjectItem.setOnAction((ActionEvent t) -> {
			
			//set up the new window contents
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER_LEFT);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25,25,25,25));
			
			//create a new window
			Scene newProjectWindow = new Scene(grid, 400, 300);
			Stage nStage = new Stage();
			nStage.setTitle("New Project Settings");		

			//set up labels and text fields and add them to the window
			Label projectName = new Label("Project Name: ");
			TextField projectNameField = new TextField();
			grid.add(projectName, 0, 1);
			grid.add(projectNameField, 1, 1);

			Label projectDir = new Label("Project Directory: ");
			TextField projectDirField = new TextField();
			grid.add(projectDir , 0, 2);
			grid.add(projectDirField, 1, 2);

			Label mainClass = new Label("Main Class: ");
			TextField mainClassField = new TextField();
			grid.add(mainClass, 0, 3);
			grid.add(mainClassField, 1, 3);

			//Text to display exception errors
			final Text errText = new Text();
			grid.add(errText, 1, 6);
			

			Button confirmBtn = new Button("Create");
			//Write out file with the basic project settings if possible if not tell user
			confirmBtn.setOnAction((ActionEvent a) -> {
				try{
					if(projectNameField.getText() == "" || projectDirField.getText() == ""){
						errText.setFill(Color.FIREBRICK);
						errText.setText("Missing fields");
					}else{
						File projectFile = new File(projectDirField.getText() + ".project");
						File mainClassFile = new File(projectDirField.getText() + mainClassField.getText() + ".java");
						new File(projectDirField.getText() + "libs").mkdirs(); //returns a boolean

						FileOutputStream fs = new FileOutputStream(projectFile);
						OutputStreamWriter os = new OutputStreamWriter(fs);
						Writer w = new BufferedWriter(os);
						w.write("Project Name: " + projectNameField.getText() + "\n");
						w.write("Project Directory: " + projectDirField.getText() + "\n");
						w.write("Project Libraries: " + projectDirField.getText() + "libs/\n");
						w.write("Project Main: " + projectDirField.getText() + mainClassField.getText() + ".java");
						w.close();
						
						//can setup some basic code here in the file i.e. public class Main ...
						fs = new FileOutputStream(mainClassFile);
						fs.close();

						errText.setFill(Color.GREEN);
						errText.setText("Created project files");
					}
				}catch(IOException e){
					errText.setFill(Color.FIREBRICK);
					errText.setText("Error creating project files");
				}
			});


			HBox hbBtn = new HBox(10);
			hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn.getChildren().add(confirmBtn);
			grid.add(hbBtn, 1,4);

			Button cancelBtn = new Button("Cancel");
			//close this window, not the editor
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
		openProjectItem.setOnAction((ActionEvent t) -> {
			try{
				//make a file picker
				FileChooser fc = new FileChooser();
				fc.setTitle("Open Project File");
				File projectFile = fc.showOpenDialog(stage);
				FileReader fr = new FileReader(projectFile);
				BufferedReader br = new BufferedReader(fr);
				//StringBuffer sb = new StringBuffer();

				//Read the file, all relevant items are after the : so search for that
				String pName, pDir, pLib, pMain;

				//This is the only real one we use at the moment
				pName = br.readLine();
				pName = pName.substring(pName.indexOf(":"), pName.length());

				//In the future will be used to display tree view
				pDir = br.readLine();
				pDir = pDir.substring(pDir.indexOf(":"), pDir.length());

				//In the future will be used for compiling
				pLib = br.readLine();
				pLib = pLib.substring(pLib.indexOf(":"), pLib.length());
				
				//In the future will load into text area
				pMain = br.readLine();
				pMain = pMain.substring(pMain.indexOf(":"), pMain.length());

				stage.setTitle("Project: " + pName);
			}catch(IOException e){
				//Make a window telling the user there was an error!

			}
		});

		MenuItem saveItem = new MenuItem("Save");
		saveItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
		//if the user clicks on exit or uses the shortcut, closes all widnows
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
