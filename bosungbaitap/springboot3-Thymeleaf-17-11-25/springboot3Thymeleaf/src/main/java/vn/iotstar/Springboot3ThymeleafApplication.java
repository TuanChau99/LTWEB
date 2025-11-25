package vn.iotstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import vn.iotstar.configs.CustomSiteMeshFilter;

@SpringBootApplication
@ComponentScan
public class Springboot3ThymeleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot3ThymeleafApplication.class, args);
	}
	
	@Bean

	public FilterRegistrationBean<CustomSiteMeshFilter> siteMeshFilter() {

	FilterRegistrationBean<CustomSiteMeshFilter> filterRegistrationBean = new FilterRegistrationBean<CustomSiteMeshFilter>();

	filterRegistrationBean.setFilter(new CustomSiteMeshFilter()); // adding sitemesh filter ??

	filterRegistrationBean.addUrlPatterns("/*");

	return filterRegistrationBean;

	}

}
