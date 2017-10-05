package com.algaworks.algamoney.api.token;

//As boas praticas recomenda que o cliente(javascript/angular) não tenha acesso ao refresh token. A recomendação
//é que coloque o refresh token no cookie http, para quando estivermos em produção sendo seguro(https), o javascript não tem
//acesso ao ele de jeito algum. Vamos tirar o refresh token  do corpo de resposta de quando solicitamos um access token
// e coloca-lo num cookie

//  ReeponseBodyAdvice = processador que pega a requisição antes de ser escrita de volta e apresentada ao cliente,


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.algaworks.algamoney.api.config.property.AlgamoneyApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>{
    //OAuth2AccessToken é o objeto que o Ouath retorna contendo as informações no localhost:8095/oauth/token(JSON)

    @Autowired
    private AlgamoneyApiProperty algamoneyApiProperty;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getMethod().getName().equals("postAccessToken");
    }

    @Override
    public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
                                             MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                             ServerHttpRequest request, ServerHttpResponse response) {

        HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
        HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();

        //casting para tirar o refreshtoken do body
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;

        String refreshToken = body.getRefreshToken().getValue();
        adicionarRefreshTokenNoCookie(refreshToken, req, resp);
        removerRefreshTokenDoBody(token);

        return body;
    }

    private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
        //refresh token vazio na exibicao
        token.setRefreshToken(null);
    }

    private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        //so quero que ele seja acessivo em http
        refreshTokenCookie.setHttpOnly(true);
        //Deve funcionar somente em https?
        refreshTokenCookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps());
        //pra qual caminho o cookie deverá ser enviado pelo browser
        refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
        //em quanto tempo esse cookie ira inspirar em dias
        refreshTokenCookie.setMaxAge(2592000);
        //adicionar o cookie na resposta
        resp.addCookie(refreshTokenCookie);
    }
}
