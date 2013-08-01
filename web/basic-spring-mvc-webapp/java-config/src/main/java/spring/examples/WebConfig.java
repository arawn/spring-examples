package spring.examples;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

/**
 * @author arawn.kr@gmail.com
 */
@Configuration
@EnableWebMvc
@ComponentScan("spring.examples.web")
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * CSS / JavaScript / Image 등의 정적 리소스를 처리해주는 핸들러를 등록
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("/resources/favicon.ico");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 404 오류가 발생했을때 보여줄 뷰를 등록
        registry.addViewController("/page-not-found").setViewName("errors/404");
    }

    /**
     * JSP를 뷰로 사용하는 뷰 리졸버 등록
     */
    @Bean
    public ViewResolver jstlViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }

    /**
     * {@link org.springframework.core.env.Environment}를 사용해 빈 주입 값을 치환
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}