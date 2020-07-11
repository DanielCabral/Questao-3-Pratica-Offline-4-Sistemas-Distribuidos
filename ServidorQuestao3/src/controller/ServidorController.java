package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

public class ServidorController implements Initializable {
	@FXML
	ImageView imagem;
	
	@FXML
	javafx.scene.control.ListView listaDeArquivos;

	HashMap<String,String> arquivos =new HashMap<String,String>();
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				ServerSocket server = null;
				try {
					server = new ServerSocket(12345);
				     while(true){
				       System.out.println("Aguardando conexão...");
				       Socket con;
	
						con = server.accept();
					
				       System.out.println("Cliente conectado...");				    
				      //Criar classe servidor que ficara escutando o novo cliente
				       Servidor servidor;
				       
				      //Instanciar passando o socket de conexão e a lista de arquivos
					   servidor = new Servidor(con,arquivos);
					   //Crio a thread que executara a classe servidor paralelamente a o ServidorController
				       Thread t1;
				       t1 = new Thread(servidor);
				       t1.start();   
				    }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		});
		t.start();
	}	
	
	@FXML
	public void handleDragOver(DragEvent e) {
		if(e.getDragboard().hasFiles()) {
			e.acceptTransferModes(TransferMode.ANY);
		}
		
	}
	
	@FXML
	public void handleDrop(DragEvent e) throws FileNotFoundException {
		List<File> files = e.getDragboard().getFiles();
		String extension = "";
		String nomeDoArquivo=files.get(0).getName();
		String caminho = files.get(0).getAbsolutePath();
		int i = caminho.lastIndexOf('.');
		
		if (i > 0) {
		    extension = caminho.substring(i+1);
		}
		
		//Verificar se arquivo é txt
		if(extension.equals("txt")) {
			System.out.println(nomeDoArquivo);
			//Adicionar a lista de arquivos -ListView da tela e hashmap
			listaDeArquivos.getItems().add(nomeDoArquivo);			
			arquivos.put(nomeDoArquivo, caminho);
		}
	}
	
	@FXML
	public void pararServidor() {
		System.exit(0);
	}
}
