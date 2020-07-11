package controller;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
public class ClienteRunnable implements Runnable{
private Socket cliente;
private boolean conexao = true;

public ClienteRunnable(Socket c){
	this.cliente = c;
}

public void run() {
	try {
		PrintStream saida;
		System.out.println("O cliente conectou ao servidor");
		//Prepara para leitura do teclado
		Scanner teclado = new Scanner(System.in);
		//Cria objeto para enviar a mensagem ao servidor
		saida = new PrintStream(this.cliente.getOutputStream());
		//Envia mensagem ao servidor
		String snd;
		
		
		while(conexao){
			System.out.println("Digite uma mensagem: ");
			snd = teclado.nextLine();
			if (snd.equalsIgnoreCase("fim"))
			conexao = false;
			else
			System.out.println(snd);
			saida.println(snd);
		}
			saida.close();
			teclado.close();
			cliente.close();
			System.out.println("Cliente finaliza conexão.");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
