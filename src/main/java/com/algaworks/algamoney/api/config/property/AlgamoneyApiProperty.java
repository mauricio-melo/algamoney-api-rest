package com.algaworks.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

    private String originPermitida = "http://localhost:4200";
    public final Seguranca seguranca = new Seguranca();

    public String getOrigemPermitida() {
        return originPermitida;
    }

    public void setOrigemPermitida(String origemPermitida) {
        this.originPermitida = origemPermitida;
    }

    public Seguranca getSeguranca() {
        return seguranca;
    }

    public static class Seguranca{

        public boolean isEnableHttps() {
            return enableHttps;
        }

        public void setEnableHttps(boolean enableHttps) {
            this.enableHttps = enableHttps;
        }

        private boolean enableHttps;

    }
}

