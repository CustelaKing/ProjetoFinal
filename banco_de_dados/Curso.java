package banco_de_dados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Curso {
	
	private StringProperty nome = new SimpleStringProperty("");
	private IntegerProperty codigo = new SimpleIntegerProperty(0);
	private StringProperty ativo = new SimpleStringProperty("");
	
	
	public static ArrayList<Curso> listarTodas(Connection conn, String iniciais){
		ArrayList<Curso> lista = new ArrayList<Curso>();
		try {
			String sql = "select * from curso where ativo='S' order by nome";
			if(iniciais!=null){
				sql = "select * from curso "
					+ "where ativo ='S' and "
					+ "nome like '%"+iniciais+"%' order by nome";
			}
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Curso c = new Curso();
				c.setCodigo(rs.getInt("codigo"));
				c.setNome(rs.getString("nome"));
				c.setAtivo(rs.getString("ativo"));
				lista.add(c);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lista;
	}

	public void insere(Connection conn){	
		try {
			String sql = "insert into curso (nome, ativo) " + "values (?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getNome());
			ps.setString(2, "S");
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void altera(Connection conn){
		try {
			String sql = "update curso set nome=? where codigo=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getNome());
			ps.setInt(2, getCodigo());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exclui(Connection conn){
		try {
			String sql = "update curso set ativo='N' where codigo=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getCodigo());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return getNome();
	}
	
	public StringProperty nomeProperty() {
		return this.nome;
	}

	public String getNome() {
		return this.nomeProperty().get();
	}
	
	public void setNome(String nome) {
		this.nomeProperty().set(nome);
	}
	
	public IntegerProperty codigoProperty() {
		return this.codigo;
	}

	public int getCodigo() {
		return this.codigoProperty().get();
	}
	
	public void setCodigo(int codigo) {
		this.codigoProperty().set(codigo);
	}

	public StringProperty ativoProperty() {
		return this.ativo;
	}
	
	public String getAtivo() {
		return this.ativoProperty().get();
	}

	public void setAtivo(String ativo) {
		this.ativoProperty().set(ativo);
	}
	
}

