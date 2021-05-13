package com.projetojava.brewer.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import com.projetojava.brewer.thymeleaf.processor.OrderElementTagProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import com.projetojava.brewer.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import com.projetojava.brewer.thymeleaf.processor.MessageElementTagProcessor;

public class BrewerDialect extends AbstractProcessorDialect{
	
	
	
	public BrewerDialect() {
		super("AlgaWorks Brewer", "brewer", StandardDialect.PROCESSOR_PRECEDENCE);
		
	}

	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		final Set<IProcessor> processadores = new HashSet<>();
		processadores.add(new ClassForErrorAttributeTagProcessor(dialectPrefix));
		processadores.add(new MessageElementTagProcessor(dialectPrefix));
		processadores.add(new OrderElementTagProcessor(dialectPrefix));
		
		return processadores;
	}
	
	
}
