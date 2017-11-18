package banco_de_dados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Matricula {

	private StringProperty dados= new SimpleStringProperty("");

	Aluno aluno = new Aluno();
	Curso curso = new Curso();
	
	public void insere(Connection conn) {
		try {
			String sql = "insert into matricula (cod_aluno, cod_curso) " + "values (?,?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, aluno.getCodigo());
			ps.setInt(2, curso.getCodigo());
			ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Matricula> filtraPorAluno(Connection conn, Aluno a){
		ArrayList<Matricula> lista = new ArrayList<Matricula>();
		try{
			String sql = "select curso.nome as curso "
					+	"from curso, matricula "
					+	"where matricula.cod_curso = curso.codigo "
					+	"and matricula.cod_aluno = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, a.getCodigo());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				Matricula m = new Matricula();
				m.setDados(rs.getString("curso"));
				lista.add(m);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public static ArrayList<Matricula> filtraPorCurso(Connection conn, Curso c){
		ArrayList<Matricula> lista = new ArrayList<Matricula>();
		try{
			String sql = "select aluno.nome as aluno "
					+	"from aluno, matricula "
					+	"where matricula.cod_aluno = aluno.codigo "
					+	"and matricula.cod_curso = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, c.getCodigo());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				Matricula m = new Matricula();
				m.setDados(rs.getString("aluno"));
				lista.add(m);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
	
	public Aluno getAluno() {
		return aluno;
	}
	
	public void setAluno(Aluno aluno) {
		this.aluno = aluno;
	}
	
	public Curso getCurso() {
		return curso;
	}
	
	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public StringProperty dadosProperty() {
		return this.dados;
	}
	

	public String getDados() {
		return this.dadosProperty().get();
	}
	

	public void setDados(final String dados) {
		this.dadosProperty().set(dados);
	}
	
	
}
	
