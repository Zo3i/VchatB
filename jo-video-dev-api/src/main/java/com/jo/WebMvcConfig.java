package com.jo;

import com.jo.controller.interceptor.MyInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/META-INF/resources/")
				.addResourceLocations("file:E:/WeixinApp/userFile/");
	}

	@Bean
	public MyInterceptor myInterceptor() {
		return new MyInterceptor();
	}

	@Bean(initMethod = "init")
	public ZKCuratorClient zkCuratorClient() {
		return new ZKCuratorClient();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(myInterceptor()).addPathPatterns("/user/**")
					.addPathPatterns("/video/userLike", "/video/userDislike",
					                 "/video/uploadVideo", "/video/uploadVideoCover")
					.addPathPatterns("/bgm/**")
					.excludePathPatterns("/user/queryPublishInfo")
					.excludePathPatterns("/user/queryFollow")
					.excludePathPatterns("/video/getComments")
					.addPathPatterns("/video/saveComment");

		super.addInterceptors(registry);
	}
}
