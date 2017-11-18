package banco_de_dados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Aluno {
	private IntegerProperty codigo = new SimpleIntegerProperty(0);
	private StringProperty nome = new SimpleStringProperty("");
	private IntegerProperty idade = new SimpleIntegerProperty(0);
	private StringProperty sexo = new SimpleStringProperty("");
	private StringProperty ativo = new SimpleStringProperty("");
	private Cidade cidade = new Cidade();
	
	public static ArrayList<Aluno> listarTodas(Connection conn, String iniciais){
		ArrayList<Aluno> lista = new ArrayList<Aluno>();
		try {
			String sql = "select aluno.codigo as cdAluno, "
					+ "aluno.nome as nmAluno, idade, sexo, "
					+ "cidade.nome as nmCidade, uf, "
					+ "cidade.codigo as cdCidade, "
					+ "cidade.ativo as ativoCidade, "
					+ "aluno.ativo as ativoAluno "
					+ "from aluno, cidade "
					+ "where cidade.codigo = aluno.cidade "
					+ "and aluno.ativo='S' "
					+ "order by aluno.nome";
			if(iniciais!=null){
				sql = "select aluno.codigo as cdAluno, "
						+ "aluno.nome as nmAluno, idade, sexo, "
						+ "cidade.codigo as cdCidade, "
						+ "cidade.nome as nmCidade, uf, "
						+ "cidade.ativo as ativoCidade, "
						+ "aluno.ativo as ativoAluno "
						+ "from aluno, cidade "
						+ "where cidade.codigo = aluno.cidade "
						+ "and aluno.ativo='S' and "
						+ "aluno.nome like '%"+iniciais+"%' " 
						+ "order by aluno.nome";
			}
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				
				Cidade c = new Cidade();
				c.setCodigo(rs.getInt("cdCidade"));
				c.setNome(rs.getString("nmCidade"));
				c.setUf(rs.getString("uf"));
				c.setAtivo(rs.getString("ativoCidade"));
				
				Aluno a = new Aluno();
				a.setCodigo(rs.getInt("cdAluno"));
				a.setNome(rs.getString("nmAluno"));
				a.setIdade(rs.getInt("idade"));
				a.setSexo(rs.getString("sexo"));
				a.setCidade(c);
				a.setAtivo(rs.getString("ativoAluno"));
				
				lista.add(a);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lista;
	}
	
	public void insere(Connection conn) {
		try {
			String sql = "insert into aluno (nome, sexo, idade, ativo, cidade) " + "values (?,?,?,?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getNome());
			ps.setString(2, getSexo());
			ps.setInt(3, getIdade());
			ps.setString(4, "S");
			ps.setInt(5, getCidade().getCodigo());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void altera(Connection conn){
		try {
			String sql = "update aluno set nome=?, sexo=?, idade=?, cidade=? where codigo=?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getNome());
			ps.setString(2, getSexo());
			ps.setInt(3, getIdade());
			ps.setInt(4, getCidade().getCodigo());
			ps.setInt(5, getCodigo());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exclui(Connection conn){
		try {
			String sql = "update aluno set ativo='N' where codigo=?";
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
	
	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
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

	public IntegerProperty idadeProperty() {
		return this.idade;
	}

	public int getIdade() {
		return this.idadeProperty().get();
	}

	public void setIdade(int idade) {
		this.idadeProperty().set(idade);
	}

	public StringProperty sexoProperty() {
		return this.sexo;
	}

	public String getSexo() {
		return this.sexoProperty().get();
	}

	public void setSexo(String sexo) {
		this.sexoProperty().set(sexo);
	}

	public IntegerProperty codigoProperty() {
		return this.codigo;
	}
	

	public int getCodigo() {
		return this.codigoProperty().get();
	}
	

	public void setCodigo(final int codigo) {
		this.codigoProperty().set(codigo);
	}
	

	public StringProperty ativoProperty() {
		return this.ativo;
	}
	

	public String getAtivo() {
		return this.ativoProperty().get();
	}
	

	public void setAtivo(final String ativo) {
		this.ativoProperty().set(ativo);
	}
	
}
