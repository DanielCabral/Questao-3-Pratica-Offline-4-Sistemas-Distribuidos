package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

public class ClienteController implements Initializable {
	@FXML
	ImageView imagem;
	
	@FXML
	javafx.scene.control.ListView<String> listaDeArquivos;

	@FXML
	TextArea conteudoArquivo;
	
	public ObservableList<String> obList;
	
	String itemSelecionadoNaLista;
	Socket socket=null;
	ArrayList<String> arquivosDoServidor =new ArrayList<String>();
	
	
	Thread escuta;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			socket = new Socket("127.0.0.1", 12345);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InetAddress inet = socket.getInetAddress();
		System.out.println("HostAddress = "+inet.getHostAddress());
		System.out.println("HostName = "+inet.getHostName());
		
		//Escutar servidor
				  escuta=new Thread(new Runnable() {
					
					@Override
					public void run() {
						try {
							Object rcv;
							Socket s=socket;
							
							while(true){
								//Receber objeto do fluxo do socket
								ObjectInputStream is=new ObjectInputStream(s.getInputStream());
								rcv = is.readObject();
							
								if(rcv instanceof Arquivo) {
									Arquivo arquivo= (Arquivo)  rcv;									
									conteudoArquivo.setText(arquivo.getConteudo()); 
								}
								
								if(rcv instanceof ArrayList) {
									arquivosDoServidor= (ArrayList<String>) rcv;
									listaDeArquivos.getItems().clear();
									listaDeArquivos.getItems().addAll(arquivosDoServidor);
								}
								is=null;
								
							}
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
			escuta.start();
		
		
		 listaDeArquivos.getSelectionModel().selectedItemProperty().addListener(
		                (observable, oldValue, newValue) -> {
							try {
								selecionarItemTableViewItemDeOrcamento(newValue);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
	}	
	
	public String selecionarItemTableViewItemDeOrcamento(Object  newValue) throws IOException{
		itemSelecionadoNaLista= (String) newValue;
		PrintStream saida = new PrintStream(this.socket.getOutputStream());	
		saida.println(itemSelecionadoNaLista);
        return itemSelecionadoNaLista;
     }
	
	@FXML
	public void encerrarConexao() throws IOException {
		PrintStream saida = new PrintStream(this.socket.getOutputStream());	
		saida.println("fim");
		escuta.stop();
		System.exit(0);
	}
	
}
