package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca


            ;
    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }
    public void exibeMenu(){
        var opcao= -1;
        while (opcao != 0){
            var menu = """
                \nExibição de Menu: \n
                01- Buscar series
                02- Buscar episódios
                03- Listar séries buscadas
                04- Buscar série por titulo
                05- Buscar série por ator
                06- Top 5 série
                07- Buscar séries por categoria
                08- Bucar série por temporada
                09- Buscar episodio por trecho
                10- Top epidosios por series
                11- Buscar episodios por data lançamento
                
                0- sair
                
                Escolha a opção desejada:
                """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscaSerieWeb();
                    break;
                case 2:
                    buscaEpisorioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 8:
                    buscarSeriePorTemporada();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    episodiosDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void episodiosDepoisDeUmaData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento: ");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();


            List<Episodio> episodiosAno = repositorio.episodioPorSerieEPorAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach( e->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporadas(),
                            e.getNumeroEpisodio(), e.getTitulo()));
        }

    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite o nome do trecho: ");
        var trecho = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trecho);
        episodiosEncontrados.forEach(e ->
        System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                e.getSerie().getTitulo(), e.getTemporadas(),
                e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
    }

    private void buscarSeriePorTemporada() {
        System.out.println("Digite a quantidade de temporadas: ");
        var numeroTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Avaliação a partir de: ");
        var avaliacaoMaxima = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.buscarSeriePorTemporadasEAvaliacao(numeroTemporadas, avaliacaoMaxima);
        System.out.println("Series com o maximo de temporadas de " + numeroTemporadas + " encontradas: ");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + "Temporadas: " + s.getTotalTemporadas() + " / Avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorCategoria() {
        System.out.println("Digite a categoria/genero desejado: ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriePorCategoria = repositorio.findByGenero(categoria);
        System.out.println("series da categoria" + categoria);
        seriePorCategoria.forEach(System.out::println);
    }

    private void buscarTop5Series() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s ->
                System.out.println(s.getTitulo() + "Avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorAtor() {
        System.out.println("Qual o nome do Ator? ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de que valor?");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Série em que " + nomeAtor + " Trabalhou: ");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + "Avaliação: " + s.getAvaliacao()));
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma seire pelo nome: ");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()){
            System.out.println("Dados da serie: " + serieBusca.get());
        }else {
            System.out.println("Serie não encontrada!");
        }
    }

    private DadosSerie getDadosSerie(){
        System.out.println("Digite o nome da serie para busca: ");
        var nomeSerie = leitura.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscaSerieWeb(){
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private void buscaEpisorioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma seire pelo nome: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()){

            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i=1; i<=serieEncontrada.getTotalTemporadas(); i++){
                var json = consumoApi.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Serie não encontrada!");
        }

    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}
