package br.com.alura.screenmatch.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer temporadas;
    private String titulo;
    private Integer numeroEpisodio;
    private Double avaliacao;
    private LocalDate dataLancamento;

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio){
        this.temporadas = numeroTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.numero();

        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        }catch (NumberFormatException ex){
            this.avaliacao = 0.0;
        }

        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        }catch (DateTimeParseException ex){
            this.dataLancamento = null;
        }

    }

    public void setTemporadas(Integer temporadas) {
        this.temporadas = temporadas;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }


    public Integer getTemporadas() {
        return temporadas;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    @Override
    public String toString() {
        return "temporadas=" + temporadas +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento;
    }
}
