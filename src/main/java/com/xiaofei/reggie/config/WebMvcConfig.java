package com.xiaofei.reggie.config;

import com.xiaofei.reggie.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        WebMvcConfigurer.super.extendMessageConverters(converters);
        converters.add(5, messageConverter);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/static/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/static/front/");
        registry.addResourceHandler("/qy/**").addResourceLocations("classpath:/static/qy/");

    }

    //配置服务器跨域请求被允许
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
//                .allowCredentials(true).maxAge(3600);
//    }


 //   @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry){
//        registry.addResourceHandler("/**");
////        registry.addResourceHandler ("/backend/**").addResourceLocations("classpath:/static/backend" );
////        registry.addResourceHandler ("/front/**"). addResourceLocations("classpath:/static/front");
//    }
}
