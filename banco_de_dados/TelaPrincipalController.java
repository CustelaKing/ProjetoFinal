package banco_de_dados;

import java.sql.Connection;
import java.util.ArrayList;

import exceptions.TemNumeroException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import metodo.Mensagens;

public class TelaPrincipalController {
	
	//Cadastro de alunos
	@FXML private TextField txtAluno;
    @FXML private TextField txtIdade;
    @FXML private TextField txtFiltroA;
    @FXML private RadioButton rbFem;
    @FXML private RadioButton rbMasc;
    @FXML private ComboBox<Cidade> cidadeA;
	@FXML private TableView<Aluno> tblAluno;
	@FXML private TableColumn<Aluno, String> colNomeA;
	@FXML private TableColumn<Aluno, String> colCidadeA;
	@FXML private TableColumn<Aluno, String> colUFA;
	@FXML private TableColumn<Aluno, Number> colIdade;
	@FXML private TableColumn<Aluno, String> colSexo;
	
    //Cadastro de cursos
    @FXML private TextField txtCurso;
    @FXML private TextField txtFiltroCurso;
	@FXML private TableView<Curso> tblCurso;
    @FXML private TableColumn<Curso, String> colCurso;

	//Cadastro de cidades
    @FXML private TextField txtFiltroCidade;
    @FXML private TextField txtNome;
    @FXML private ComboBox<String> txtUf;
	@FXML private TableView<Cidade> tblCidade;
    @FXML private TableColumn<Cidade, String> colNome;
    @FXML private TableColumn<Cidade, String> colUF;
    
    //Matrícula de alunos
    @FXML private ComboBox<Aluno> matriAluno;
    @FXML private ComboBox<Curso> matriCurso;
    @FXML private TableView<Matricula> tblMatricula;
    @FXML private TableColumn<Matricula, String> colResultado;
    
    //ArrayLists
    private ArrayList<String> estados = new ArrayList<>();
    private ArrayList<Cidade> cidades = new ArrayList<>();
    private ArrayList<Curso> cursos = new ArrayList<>();
    private ArrayList<Aluno> alunos = new ArrayList<>();
    private ArrayList<Matricula> busca = new ArrayList<>();
    
    private Connection conn = Conexao.conn();
    
    //ComboBox de Estados
    private void populaEstados(){
    	estados.add("SP");
    	estados.add("SC");
    	estados.add("RS");
    	estados.add("PR");
    	estados.add("RJ");
    	estados.add("MG");
    	estados.add("GO");
    	estados.add("ES");
    }
    
    //Inicializa as tabelas
    @FXML
    public void initialize() {
    	//Preenche combobox
    	populaEstados();
		txtUf.setItems(FXCollections.observableArrayList(estados));
		
		//Colunas cidade
		colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
    	colUF.setCellValueFactory(cellData -> cellData.getValue().ufProperty());
    	
    	//Coluna curso
    	colCurso.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
    	
    	//Colunas aluno
    	colNomeA.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
    	colSexo.setCellValueFactory(cellData -> cellData.getValue().sexoProperty());
    	colIdade.setCellValueFactory(cellData -> cellData.getValue().idadeProperty());
    	colCidadeA.setCellValueFactory(cellData -> cellData.getValue().getCidade().nomeProperty());
    	colUFA.setCellValueFactory(cellData -> cellData.getValue().getCidade().ufProperty());
    	
    	//Colunas matricula
    	colResultado.setCellValueFactory(cellData -> cellData.getValue().dadosProperty());
    	
    	//Atualiza tabelas
    	attTblAluno();
    	attTblCurso();
    	attTblCidade();

    	//Preenche ComboBox com o método toString
    	abaAluno();
    	attMatricula();       	
    }

    //Atualiza a tabela de alunos
    @FXML
    private void attTblAluno(){
    	alunos = Aluno.listarTodas(conn, null);
    	tblAluno.setItems(FXCollections.observableArrayList(alunos));
    }
    
    //Preenche os campos quando clica no item da tabela
    @FXML
    public void clicaTblAluno(){
    	Aluno sel = tblAluno.getSelectionModel().getSelectedItem();
    	txtAluno.setText(sel.getNome());
    	txtIdade.setText(sel.getIdade()+"");
    	cidadeA.getSelectionModel().select(sel.getCidade());
    	
    	if(sel.getSexo().equals("M"))
    		rbMasc.setSelected(true);
    	else
    		rbFem.setSelected(true);
    }
    
    //Atualiza ComboBox quando abre a aba de aluno
    @FXML
    public void abaAluno(){
    	cidades = Cidade.listarTodas(conn, null);
    	cidadeA.setItems(FXCollections.observableArrayList(cidades));	
    }
    
    //Ainda em teste txtAluno
    @FXML
    public void filtraCarac(){
    	
    	//Cadastro aluno
    	if(txtAluno.getText().matches(".*\\d+.*")){
			txtAluno.setStyle("-fx-text-fill: red;");
    	}else{
    		txtAluno.setStyle("-fx-text-fill: black;");}
    	
    	if(txtIdade.getText().matches(".*[a-z].*")){
			txtIdade.setStyle("-fx-text-fill: red;");
    	}else{
    		txtIdade.setStyle("-fx-text-fill: black;");}
    	
    	//Cadastro cidade
    	if(txtNome.getText().matches(".*\\d+.*")){
			txtNome.setStyle("-fx-text-fill: red;");
    	}else{
			txtNome.setStyle("-fx-text-fill: black;");}

    	//Cadastro curso
    	if(txtCurso.getText().matches(".*\\d+.*")){
			txtCurso.setStyle("-fx-text-fill: red;");
    	}else{
			txtCurso.setStyle("-fx-text-fill: black;");}
    }	
    
    //Insere um novo aluno
    @FXML
    public void insereAluno(){
    	try{
    		if(txtAluno.getText().equals(""))
    			throw new NullPointerException();
		
    		if(txtAluno.getText().matches(".*\\d+.*"))
    			throw new TemNumeroException();
    		
    		if(!rbMasc.isSelected()){
    			if(!rbFem.isSelected())
    				throw new NullPointerException();
    		}
    		
    		Aluno a = new Aluno();
    		a.setNome(txtAluno.getText());
    		a.setIdade(Integer.parseInt(txtIdade.getText()));
    		a.setSexo(rbMasc.isSelected()?"M":"F");
    		a.setCidade(cidadeA.getSelectionModel().getSelectedItem());
    		a.insere(conn);
    		alunos = Aluno.listarTodas(conn, null);
    		tblAluno.setItems(FXCollections.observableArrayList(alunos));
    		attTblAluno();
    		limpaAluno();
    		Mensagens.msgInformacao("Parabéns","Aluno cadastrado com sucesso");
    		
    	}catch (TemNumeroException e){
    		Mensagens.msgErro("FALHA","O campo destacado não pode conter números");    
    	}catch (NullPointerException e){
    		Mensagens.msgErro("FALHA","Preencha todos os campos");    
    	}catch (NumberFormatException e){
    		Mensagens.msgErro("FALHA","Erro de conversão numérica");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
  //Alteração do aluno selecionado
    @FXML
    public void alteraAluno(){
    	Aluno sel = tblAluno.getSelectionModel().getSelectedItem();
    	sel.setNome(txtAluno.getText());
    	sel.setIdade(Integer.parseInt(txtIdade.getText()));
    	sel.setCidade(cidadeA.getSelectionModel().getSelectedItem());
    	sel.setSexo(rbMasc.isSelected()?"M":"F");
    	sel.altera(conn);
    	attTblAluno();
    }
    
  //Inativação do Aluno selecionado
    @FXML
    public void excluiAluno(){
    	if(Mensagens.msgExcluir()){
        	Aluno sel = tblAluno.getSelectionModel().getSelectedItem();
        	sel.exclui(conn);
        	attTblAluno();
        	limpaAluno();
    	}else{
	    	Mensagens.msgInformacao("Cancelado","Aluno não excluído");
    	}
    }
    
    //Filtro do aluno
    @FXML
    public void filtraAluno(){
    	String filtro = txtFiltroA.getText();
       	
       	if(filtro.equals(""))
       		filtro=null;
       	
       	alunos = Aluno.listarTodas(conn, filtro);
		tblAluno.setItems(FXCollections.observableArrayList(alunos));
    }
    
    //Reseta todos os campos do aluno
    @FXML
    public void limpaAluno(){
    	txtAluno.setText("");
    	rbMasc.setSelected(false);
    	rbFem.setSelected(false);
    	txtIdade.setText("");
    	cidadeA.getSelectionModel().clearSelection();
    }
    
    //Insere uma nova cidade
    @FXML
    public void insereCidade(){
     try{ 	
    	if(txtNome.getText().equals(""))
    		throw new NullPointerException();
    	
    	if(txtNome.getText().matches(".*\\d+.*"))
    		throw new TemNumeroException();
    	
    	if(txtUf.getSelectionModel().isEmpty())
    		throw new NullPointerException();

    	Cidade c = new Cidade();
    	c.setNome(txtNome.getText());
    	c.setUf(txtUf.getSelectionModel().getSelectedItem());
    	c.insere(conn);
    	attTblCidade();
    	limpaCidade();
    	Mensagens.msgInformacao("Parabéns","Cidade cadastrada com sucesso");
    	
     	}catch (TemNumeroException e){
     		Mensagens.msgErro("FALHA","O campo destacado não pode conter números");    
 		}catch (NullPointerException e){
 			Mensagens.msgErro("FALHA","Preencha todos os campos");    
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }
  
    //Altera a cidade Selecionada
    @FXML
    public void alteraCidade(){
    	Cidade sel = tblCidade.getSelectionModel().getSelectedItem();
    	sel.setNome(txtNome.getText());
    	sel.setUf(txtUf.getSelectionModel().getSelectedItem());
    	sel.altera(conn);
    	attTblCidade();
    	limpaCidade();
    }
    
    //Exclui a cidade Selecionada
    @FXML
    public void excluiCidade(){
    	if(Mensagens.msgExcluir()){
    		Cidade sel = tblCidade.getSelectionModel().getSelectedItem();
    		sel.exclui(conn);
    		attTblCidade();
    		limpaCidade();
    	}else{
	    	Mensagens.msgInformacao("Cancelado", "Cidade não excluída");
    	}
    }
    
    //Limpa os campos da cidade
    @FXML
    public void limpaCidade(){
    	txtNome.setText("");
    	txtUf.getSelectionModel().clearSelection();
    }
  
    //Atualiza a tabela de cidades
    @FXML
    public void attTblCidade(){
    	cidades = Cidade.listarTodas(conn, null);
    	tblCidade.setItems(FXCollections.observableArrayList(cidades));
    }
    
    //Faz a filtragem de cidades
    @FXML
    public void filtraCidade(){
    	String filtro = txtFiltroCidade.getText();
    	
    	if(filtro.equals(""))
    		filtro=null;
    	
    	cidades = Cidade.listarTodas(conn, filtro);
		tblCidade.setItems(FXCollections.observableArrayList(cidades));	
	}
    
    //Preenche os campo de texto quando clica no item
    @FXML
    public void clicaTblCidade(){
    	Cidade sel = tblCidade.getSelectionModel().getSelectedItem();
    	txtNome.setText(sel.getNome());
    	txtUf.getSelectionModel().select(sel.getUf());
    }
 
    //Insere um curso
    @FXML
    public void insereCurso(){
    	try{
    		if(txtCurso.getText().equals(""))
    			throw new NullPointerException();
    		
    		if(txtCurso.getText().matches(".*\\d+.*"))
    			throw new TemNumeroException();
    		
    		Curso c = new Curso();
    		c.setNome(txtCurso.getText());
    		c.insere(conn);
    		attTblCurso();
    		txtCurso.setText("");
    		
    	}catch (TemNumeroException e){
    		Mensagens.msgErro("FALHA","O campo destacado não pode conter números");    
    	}catch (NullPointerException e){
    		Mensagens.msgErro("FALHA","Preencha todos os campos");    
    	}catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
   //Modifica o curso selecionado
   @FXML
   public void alteraCurso(){
	   Curso sel = tblCurso.getSelectionModel().getSelectedItem();
	   sel.setNome(txtCurso.getText());
	   sel.altera(conn);
	   attTblCurso();
	   txtCurso.setText("");
   }

   //Inativa o curso selecionado
   @FXML
   public void excluiCurso(){
	   if(Mensagens.msgExcluir()){
		   Curso sel = tblCurso.getSelectionModel().getSelectedItem();
		   sel.exclui(conn);
		   attTblCurso();
		   txtCurso.setText("");
	   }else{
	    	Mensagens.msgInformacao("Cancelado","Curso não excluído");
	   }
   }
     
   //Filtragem de cursos
   @FXML
   public void filtraCurso(){
	   String filtro = txtFiltroCurso.getText();
	   
	   if(filtro.equals(""))
		   filtro=null;
	   
	   cursos = Curso.listarTodas(conn, filtro);
	   tblCurso.setItems(FXCollections.observableArrayList(cursos));
   }  

   //Preenche o campo de texto quando clica no item
   @FXML
   public void clicaTblCurso(){
	   Curso sel = tblCurso.getSelectionModel().getSelectedItem();
	   txtCurso.setText(sel.getNome());
   }
   
   //Atualiza a tabela de cursos
   @FXML
   public void attTblCurso(){
	   cursos = Curso.listarTodas(conn, null);
	   tblCurso.setItems(FXCollections.observableArrayList(cursos));
   }
   
   //Faz a matrícula do aluno
   @FXML
   public void matriculaAluno(){
	 try{	 
		 if(matriAluno.getSelectionModel().isEmpty() || matriCurso.getSelectionModel().isEmpty())
			 throw new NullPointerException();
		 
		 Matricula mat = new Matricula();
		 mat.setAluno(matriAluno.getSelectionModel().getSelectedItem());
		 mat.setCurso(matriCurso.getSelectionModel().getSelectedItem());
		 mat.insere(conn);
		 Mensagens.msgInformacao("Parabéns","O(A) aluno(a) "+mat.getAluno()+" foi matriculado no curso "+mat.getCurso()+" com sucesso!");
	 }catch (NullPointerException e){
		 Mensagens.msgErro("ERRO","Selecione os 2 campos");
	 }catch (Exception e) {
		e.printStackTrace();
	 }
   }
   
   //Atualiza os combo boxes quando a aba matricula é aberta
   @FXML
   private void attMatricula(){
	   alunos = Aluno.listarTodas(conn, null);
	   matriAluno.setItems(FXCollections.observableArrayList(alunos));
	   cursos = Curso.listarTodas(conn, null);
	   matriCurso.setItems(FXCollections.observableArrayList(cursos));
   }

   @FXML
   public void filtraPorCurso(){
	   Curso c = matriCurso.getSelectionModel().getSelectedItem();
	   if(c==null){
		   Mensagens.msgErro("ERRO", "Selecione um curso");
	   }else{
	   busca = Matricula.filtraPorCurso(conn, c);
	   tblMatricula.setItems(FXCollections.observableArrayList(busca));
	   }   
   }
   
   @FXML
   public void filtraPorAluno(){
	   Aluno a = matriAluno.getSelectionModel().getSelectedItem();
	   if(a==null){
		   Mensagens.msgErro("ERRO", "Selecione um aluno");
	   }else{
	   busca = Matricula.filtraPorAluno(conn, a);
	   tblMatricula.setItems(FXCollections.observableArrayList(busca));
	   }   
   }
   
}