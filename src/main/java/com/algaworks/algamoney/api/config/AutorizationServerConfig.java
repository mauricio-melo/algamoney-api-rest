package com.algaworks.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AutorizationServerConfig extends AuthorizationServerConfigurerAdapter{


    //ele pega o usuario e senha da aplicação da classe Resource
    @Autowired
    private AuthenticationManager authenticationManager;

    //autorizar o cliente a acessar
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //cliente em memória
        clients.inMemory()
                    //nome do cliente
                    .withClient("angular")
                    //senha do cliente
                    .secret("@ngul@r0")
                    //scopo do cliete, limitar o acesso
                    .scopes("read", "write")
                    //cliente(angular/javascript) envia usuario e senha pra aplicação para receber o token, token atualizado
                    .authorizedGrantTypes("password", "refresh_token")
                    //quanto tempo o token ficará funcionando
                    .accessTokenValiditySeconds(1800)
                    //1 dia para espirar o refresh token
                    .refreshTokenValiditySeconds(3600 * 24)
                .and()
                    .withClient("mobile")
                    .secret("m0b1l30")
                    .scopes("read")
                    .authorizedGrantTypes("password", "refresh_token")
                    .accessTokenValiditySeconds(1800)
                    .refreshTokenValiditySeconds(3600 * 24);

                //DIFERENTES CLIENTES COM PERMISSÕES DIFERENTES
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //onde aarmazenar  o token
                .tokenStore(tokenStore())
                //conversor de token
                .accessTokenConverter(accessTokenConverter())
                //sempre que eu pedir um novo access token, um novo sera enviado(refresh token nao expira, caso esteja sendo usado)
                .reuseRefreshTokens(false)
                //validar se esta tudo certo com o usuario e senha
                .authenticationManager(authenticationManager);
    }

    @Bean
    public JwtAccessTokenConverter  accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        //chave que valida o token
        accessTokenConverter.setSigningKey("algaworks");
        return accessTokenConverter;
    }


    @Bean
    public TokenStore tokenStore(){
        //token em memória
        return new JwtTokenStore(accessTokenConverter());
    }

}
