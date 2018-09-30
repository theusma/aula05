package br.usjt.ads.arqdes.model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import br.usjt.ads.arqdes.model.entity.Filme;
import br.usjt.ads.arqdes.model.entity.Genero;
import br.usjt.arq.model.entity.Filme;

public class FilmeDAO {
	
	public int inserirFilme(Filme filme) throws IOException {
		int id = -1;
		String sql = "insert into Filme (titulo, descricao, diretor, posterpath, "
				+ "popularidade, data_lancamento, id_genero) values (?,?,?,?,?,?,?)";
		
		try(Connection conn = ConnectionFactory.getConnection();
			PreparedStatement pst = conn.prepareStatement(sql);){
			
			pst.setString(1, filme.getTitulo());
			pst.setString(2, filme.getDescricao());
			pst.setString(3, filme.getDiretor());
			pst.setString(4, filme.getPosterPath());
			pst.setDouble(5, filme.getPopularidade());
			if(filme.getDataLancamento() != null) {
				pst.setDate(6, new java.sql.Date(filme.getDataLancamento().getTime()));
			} else {
				pst.setDate(6,  null);
			}
			pst.setInt(7, filme.getGenero().getId());			
			pst.execute();
			
			//obter o id criado
			String query = "select LAST_INSERT_ID()";
			try(PreparedStatement pst1 = conn.prepareStatement(query);
				ResultSet rs = pst1.executeQuery();){

				if (rs.next()) {
					id = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
		return id;
	}
	
	
	public void excluir(Filme filme) throws IOException {
		String sqlDelete = "DELETE FROM Filme WHERE id = ?";
		// usando o try with resources do Java 7, que fecha o que abriu
		try (PreparedStatement stm = conn.prepareStatement(sqlDelete);) {
			stm.setInt(1, filme.getId());
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}

	public Filme carregar(int id) throws IOException {
		Filme Filme = new Filme();
		Filme.setId(id);
		String sqlSelect = "SELECT titulo, descricao, diretor, posterpath, popularidade, data_lancamento, id_genero;
		// usando o try with resources do Java 7, que fecha o que abriu
		try (PreparedStatement stm = conn.prepareStatement(sqlSelect);) {
			stm.setInt(1, id);
			try (ResultSet rs = stm.executeQuery();) {
				if (rs.next()) {
					Filme.setNome(rs.getString("titulo"));
					Filme.setFone(rs.getString("descricrao"));
					Filme.setEmail(rs.getString("diretor"));
					Filme.setEmail(rs.getString("posterpath"));
					Filme.setEmail(rs.getString("popularidade"));
					Filme.setEmail(rs.getString("data_lancamento"));
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new IOException(e);
			}
		} catch (SQLException e1) {
			System.out.print(e1.getStackTrace());
			throw new IOException(e1);
		}
		return Filme;
	}

	public Filme buscarFilme(int id) throws IOException{
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Filme> listarFilmes(String chave) throws IOException {
		ArrayList<Filme> lista = new ArrayList<>();
		String sql = "select f.id, f.titulo, f.descricao, f.diretor, f.posterpath, "
				+ "f. popularidade, f.data_lancamento, f.id_genero, g.nome "
				+ "from filme f, genero g "
				+ "where f.id_genero = g.id and upper(f.titulo) like ?";
		try(Connection conn = ConnectionFactory.getConnection();
			PreparedStatement pst = conn.prepareStatement(sql);){
			
			pst.setString(1, "%" + chave.toUpperCase() + "%");
		
			try(ResultSet rs = pst.executeQuery();){
			
				Filme filme;
				Genero genero;
				while(rs.next()) {
					filme = new Filme();
					filme.setId(rs.getInt("f.id"));
					filme.setTitulo(rs.getString("f.titulo"));
					filme.setDescricao(rs.getString("f.descricao"));
					filme.setDiretor(rs.getString("f.diretor"));
					filme.setPosterPath(rs.getString("f.posterpath"));
					filme.setDataLancamento(rs.getDate("f.data_lancamento"));
					genero = new Genero();
					genero.setId(rs.getInt("f.id_genero"));
					genero.setNome(rs.getString("g.nome"));
					filme.setGenero(genero);
					lista.add(filme);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
				
		return lista;
	}
	
	public ArrayList<Filme> listarFilmes() throws IOException {
		ArrayList<Filme> lista = new ArrayList<>();
		String sql = "select f.id, f.titulo, f.descricao, f.diretor, f.posterpath, "
				+ "f. popularidade, f.data_lancamento, f.id_genero, g.nome "
				+ "from filme f, genero g "
				+ "where f.id_genero = g.id";
		try(Connection conn = ConnectionFactory.getConnection();
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();){
			
			Filme filme;
			Genero genero;
			while(rs.next()) {
				filme = new Filme();
				filme.setId(rs.getInt("f.id"));
				filme.setTitulo(rs.getString("f.titulo"));
				filme.setDescricao(rs.getString("f.descricao"));
				filme.setDiretor(rs.getString("f.diretor"));
				filme.setPosterPath(rs.getString("f.posterpath"));
				filme.setDataLancamento(rs.getDate("f.data_lancamento"));
				genero = new Genero();
				genero.setId(rs.getInt("f.id_genero"));
				genero.setNome(rs.getString("g.nome"));
				filme.setGenero(genero);
				lista.add(filme);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IOException(e);
		}				
		return lista;
	}

}
