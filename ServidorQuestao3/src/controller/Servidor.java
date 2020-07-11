package controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Scanner;

import javax.swing.JOptionPane;


public class Servidor implements Runnable{
public Socket socketCliente;
static HashMap<String,String> listaDeArquivos;
Arquivo arquivo;
private long tamanhoPermitidoKB = 5120; 
public static int cont = 0;

public Servidor(Socket cliente, HashMap<String,String> listaDeArquivos) throws IOException{
	this.socketCliente = cliente;
	this.listaDeArquivos=listaDeArquivos;
	enviar(listaDeArquivos);
}




public Socket getCliente() {
	return socketCliente;
}
/* A classe Thread, que foi instancia no servidor, implementa Runnable.
 Então você terá que implementar sua lógica de troca de mensagens dentro deste método 'run'. */
 public void run(){
	System.out.println("Conexao "+Servidor.cont+" com o cliente " +
	this.socketCliente.getInetAddress().getHostAddress() +"/" +this.socketCliente.getInetAddress().getHostName());
	try {
		Scanner s = null;
		s = new Scanner(this.socketCliente.getInputStream());
		String rcv;
		 //Exibe mensagem no console
		while(s.hasNextLine()){
			rcv = s.nextLine();
			if (rcv.equalsIgnoreCase("fim"))
				break;
			else
				System.out.println(rcv);
			enviar(rcv);
	
		}
		//Finaliza scanner e socket
		s.close();
		System.out.println("Fim do cliente "+this.socketCliente.getInetAddress());
		this.socketCliente.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
 
 public void sendMessage(String mensagem) throws IOException {
	 ObjectOutputStream oss=new ObjectOutputStream(socketCliente.getOutputStream());
	 	
	 oss.writeObject(mensagem);
 }
 
 public void enviar(HashMap<String,String> ldearquivos) throws IOException {
	 ArrayList<String> listaDeArquivos1 = new ArrayList<String>(ldearquivos.keySet());
	 ObjectOutputStream oss=new ObjectOutputStream(socketCliente.getOutputStream());
	 	
	 oss.writeObject(listaDeArquivos1);
 }
 
 public void enviar(String nomeArquivo) throws IOException {
	 String caminho = listaDeArquivos.get(nomeArquivo);
	 File myFile=null;
	 try {
	     // envia o arquivo (transforma em byte array)
	     myFile = new File (caminho);

	     FileReader file=new FileReader(myFile);
	     BufferedReader bufferedReader=new BufferedReader(file);    
	     
	     //Entrada guardará todo o conteudo do arquivo. 
	     String entrada="";
	     String linha = bufferedReader.readLine(); 
	     
	     while(linha != null) {
	    	 entrada+=linha;
	    	 linha = bufferedReader.readLine(); 
	     }
	     
	     file.close();
	     bufferedReader.close();
	     
	     arquivo=new Arquivo();
	     arquivo.setNome("Arquivo.txt");
	     arquivo.setConteudo(entrada);
	     
	     ObjectOutputStream oss = new ObjectOutputStream(socketCliente.getOutputStream());
	     oss.writeObject(arquivo);
	     
	 }
	 catch(IOException e) {
		 System.out.println(e+" caminho: "+myFile.getAbsolutePath());
	 }
 }
 
 private void enviarArquivoServidor(){
	   if (validaArquivo()){
	    try { 
	        BufferedOutputStream bf = new BufferedOutputStream
	        (socketCliente.getOutputStream());
	 
	        byte[] bytea = serializarArquivo();
	        bf.write(bytea);
	        bf.flush();
	        bf.close();
	    } catch (UnknownHostException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	   }
	}
	 
	private byte[] serializarArquivo(){
	   try {
	      ByteArrayOutputStream bao = new ByteArrayOutputStream();
	      ObjectOutputStream ous;
	      ous = new ObjectOutputStream(bao);
	      ous.writeObject(arquivo);
	      return bao.toByteArray();
	   } catch (IOException e) {
	      e.printStackTrace();
	   }
	 
	   return null;
	}
	 
	private boolean validaArquivo(){
	   if (arquivo.getTamanhoKB() > tamanhoPermitidoKB){
	      JOptionPane.showMessageDialog(null,
	       "Tamanho máximo permitido atingido ("+(tamanhoPermitidoKB/1024)+")");
	      return false;
	   }else{
	      return true;
	   }
	}
 
}