package academy.devdojo.springboot2.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration//Aplica configuração globalmente no Spring
public class DevDojoWebMvcConfigurer implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageHander = new PageableHandlerMethodArgumentResolver();
        //seta o numero da pagina e o numero de elementos que serão exibidos por padrao
        pageHander.setFallbackPageable(PageRequest.of(0,5));
        resolvers.add(pageHander);
    }

}
