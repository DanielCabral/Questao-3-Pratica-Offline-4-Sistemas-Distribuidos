package controller;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("/view/FXMLVBoxMain.fxml"));			
			Scene scene = new Scene(root,600,400);
			primaryStage.setScene(scene);
			primaryStage.resizableProperty().setValue(Boolean.FALSE);
			primaryStage.setTitle("Cliente");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void caixaDeInformacao(String titulo,String cabecalho,String conteudo,int tipo) {
		Alert dialogoInfo;
		if(tipo==0) {
			dialogoInfo= new Alert(Alert.AlertType.CONFIRMATION);
		}else if(tipo==1){
			dialogoInfo= new Alert(Alert.AlertType.INFORMATION);
		}else if(tipo==2){
			dialogoInfo= new Alert(Alert.AlertType.WARNING);	
		}else {
			dialogoInfo= new Alert(Alert.AlertType.ERROR);
		}
        dialogoInfo.setTitle(titulo);
        dialogoInfo.setHeaderText(cabecalho);
        dialogoInfo.setContentText(conteudo);
        dialogoInfo.showAndWait();
	}
	public static void main(String[] args) {	
		launch(args);
	}
}
