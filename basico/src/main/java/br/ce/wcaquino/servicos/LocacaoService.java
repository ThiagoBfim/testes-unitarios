package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Date;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme filme) {
		Locacao locacao = new Locacao();
		locacao.setFilme(filme);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filme.getPrecoLocacao());

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	/**
	 * O teste precisa ser FIRST
	 * Fast				(Menos de 1 segundo)
	 * Independent		(Não depender de outros testes)
	 * Repeatable		(Retornar o mesmo resultado indepdentemente de quantas vezes for executado)
	 * Self-verifying	(O teste precisa se auto avaliar, e informar se houver erro ou não)
	 * Timely			(Deve ser criada no momento certo.)
	 * @param args
	 */
	public static void main(String[] args) {
		LocacaoService locacaoService = new LocacaoService();
		Filme filme = new Filme();
		filme.setPrecoLocacao(10.50);
		Locacao locacao = locacaoService.alugarFilme(null, filme);
		System.out.println(locacao.getValor().equals(filme.getPrecoLocacao()));
		System.out.println(DataUtils.isMesmaDataSimples(locacao.getDataRetorno(),DataUtils.adicionarDias(new Date(), 1)));
		System.out.println(DataUtils.isMesmaDataSimples(locacao.getDataLocacao(),new Date()));
	}
}