package controller;

import java.io.Serializable;
import java.util.Date;

public class Arquivo implements Serializable {
	private static final long serialVersionUID = 1L;
    
	   private String nome;
	   private String conteudo;
	   private transient long tamanhoKB;
	   public String getNome() {
	             return nome;
	   }
	   public void setNome(String nome) {
	             this.nome = nome;
	   }
	   public String  getConteudo() {
	             return conteudo;
	   }
	   public void setConteudo(String conteudo) {
	             this.conteudo = conteudo;
	   }
	   public long getTamanhoKB() {
	             return tamanhoKB;
	   }
	   public void setTamanhoKB(long tamanhoKB) {
	             this.tamanhoKB = tamanhoKB;
	   }
}
