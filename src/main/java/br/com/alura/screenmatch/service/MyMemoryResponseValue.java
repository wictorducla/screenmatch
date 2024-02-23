package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyMemoryResponseValue {
    @JsonAlias(value = "responseData")
    public DadosResposta dadosResposta;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class DadosResposta {
        @JsonAlias(value = "translatedText")
        public String textoTraduzido;
    }
}
