package banco_de_dados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Cidade {

	private StringProperty nome = new SimpleStringProperty("");
	private StringProperty uf = new SimpleStringProperty("");
	private IntegerProperty codigo = new SimpleIntegerProperty(0);
	private StringProperty ativo = new SimpleStringProperty("");
	
	public static ArrayList<Cidade> listarTodas(Connection conn, String iniciais){
		ArrayList<Cidade> lista = new ArrayList<Cidade>();
		try {
			String sql = "select * from cidade where ativo='S' order by nome";
			if(iniciais!=null){
				sql = "select * from cidade "
					+ "where ativo ='S' and "
					+ "nome like '%"+iniciais+"%' order by nome";
			}
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				Cidade c = new Cidade();
				c.setCodigo(rs.getInt("codigo"));
				c.setNome(rs.getString("nome"));
				c.setUf(rs.getString("uf"));
				c.setAtivo(rs.getString("ativo"));
				lista.add(c);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lista;
	}
	
	public void exclui(Connection conn){
		try {
			/**Excluir registro da tabela sql 
			String sql = "delete from cidade where codigo = ?";*/
			String sql = "update cidade set ativo='N' where codigo=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, getCodigo());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return getNome()+" - "+getUf();
	}
	
	public void altera(Connection conn){
		try {
			String sql = "update cidade set nome=?, uf=? where codigo=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getNome());
			ps.setString(2, getUf());
			ps.setInt(3, getCodigo());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void insere(Connection conn){
		
		try {
			String sql = "insert into cidade (nome, uf, ativo) " + "values (?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getNome());
			ps.setString(2, getUf());
			ps.setString(3, "S");
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	
	public StringProperty ufProperty() {
		return this.uf;
	}
	
	public String getUf() {
		return this.ufProperty().get();
	}
	
	public void setUf(String uf) {
		this.ufProperty().set(uf);
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
