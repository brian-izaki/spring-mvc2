package com.projetojava.brewer.config.init;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.projetojava.brewer.config.JPAConfig;
import com.projetojava.brewer.config.WebConfig;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { JPAConfig.class };
	}
	
	// retorna uma classe que ajuda o Spring a achar os controllers
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter characterEncondingFilter = new CharacterEncodingFilter();
		characterEncondingFilter.setEncoding("UTF-8");
		characterEncondingFilter.setForceEncoding(true);
		
		return new Filter[] { characterEncondingFilter };
	}

}
