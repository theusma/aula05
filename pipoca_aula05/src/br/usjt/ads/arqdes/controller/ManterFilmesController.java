package br.usjt.ads.arqdes.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.usjt.ads.arqdes.model.entity.Filme;
import br.usjt.ads.arqdes.model.entity.Genero;
import br.usjt.ads.arqdes.model.service.FilmeService;
import br.usjt.ads.arqdes.model.service.GeneroService;
import br.usjt.arq.model.entity.filme;

@Controller
public class ManterFilmesController {
	private FilmeService fService;
	private GeneroService gService;
	
	public ManterFilmesController() {
		fService = new FilmeService();
		gService = new GeneroService();
	}
	
	@RequestMapping("/")
	public String inicio() {
		return "index";
	}
	
	@RequestMapping("/inicio")
	public String inicio1() {
		return "index";
	}
	
	@RequestMapping("/voltar")
	public String voltar(HttpSession session) {
		@SuppressWarnings("unchecked")
		ArrayList<Filme> lista = (ArrayList<Filme>) session.getAttribute("lista");
		if(lista == null) {
			return "index";
		} else {
			return "ListarFilmes";
		}
	}
	
	
	
	
	@RequestMapping("/listar_filmes")
	public String listarFilmes(HttpSession session){
		session.setAttribute("lista", null);
		return "ListarFilmes";
	}
	
	@RequestMapping("/novo_filme")
	public String novoFilme(HttpSession session) {
		try {
			ArrayList<Genero> generos = gService.listarGeneros();
			session.setAttribute("generos", generos);
			return "CriarFilme";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "index";
	}
	
	@RequestMapping("/inserir_filme")
	public String inserirFilme(Filme filme, Model model) {
		try {
			Genero genero = gService.buscarGenero(filme.getGenero().getId());
			filme.setGenero(genero);
			model.addAttribute("filme", filme);
			fService.inserirFilme(filme);
			return "VisualizarFilme";
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "index";
	}
	
	@RequestMapping("salvar_filme")
	public String salvar(Filme filme, HttpSession session) throws IOException {
		filme.setId(service.criar(filme));
		ArrayList<Filme> lista = new ArrayList<>();
		lista.add(filme);
		session.setAttribute("lista", lista);
		return "Visualizarfilme";
	}

	@RequestMapping("alterar_filme")
	public String alterar(@RequestParam int id, Model model) throws IOException {
		Filme filme = service.carregar(id);
		model.addAttribute("filme", filme);
		return "Alterarfilme";
	}

	@RequestMapping("atualizar_filme")
	public String atualizar(Filme filme, HttpSession session, Model model) throws IOException {
		service.atualizar(filme);
		@SuppressWarnings("unchecked")
		ArrayList<Filme> lista = (ArrayList<Filme>) session.getAttribute("lista");
		int pos = busca(filme, lista);
		lista.remove(pos);
		lista.add(pos, filme);
		session.setAttribute("lista", lista);
		model.addAttribute("filme", filme);
		return "Visualizarfilme";
	}

	@RequestMapping("excluir_filme")
	public String excluir(@RequestParam int id, HttpSession session) throws IOException {
		filme filme = new filme();
		filme.setId(id);

		service.excluir(filme);
		@SuppressWarnings("unchecked")
		ArrayList<Filme> lista = (ArrayList<Filme>) session.getAttribute("lista");
		int pos = busca(filme, lista);
		if (pos >= 0) {
			lista.remove(pos);
		}
		session.setAttribute("lista", lista);
		return "Listarfilmes";
	}

	@RequestMapping("visualizar_filme")
	public String visualizar(@RequestParam int id, Model model) throws IOException {
		Filme filme = service.carregar(id);
		model.addAttribute("filme", filme);
		return "Visualizarfilme";
	}

	private int busca(Filme chave, ArrayList<Filme> lista) {
		for (int i = 0; i < lista.size(); i++) {
			Filme filme = lista.get(i);
			if (chave.getId() == filme.getId()) {
				return i;
			}
		}
		return -1;
	}
}


}

















