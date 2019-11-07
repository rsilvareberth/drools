package br.gov.ce.fortaleza.router;

import javax.ws.rs.HttpMethod;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.gov.ce.fortaleza.config.CamelContextManager;
import br.gov.ce.fortaleza.controller.RestApiController;

@Component
public class JmsRouter extends RouteBuilder {
	
	@Autowired
	CamelContextManager camelContextManager;
	private ProducerTemplate producerTemplate;

		 
		  
	@Override
	public void configure() throws Exception {


					
		from("direct:search-person")
		.log(LoggingLevel.INFO,log,"param: ${header.teste}")
		.bean(RestApiController.class, "executeRule(${header.teste})")
		//.marshal().json(JsonLibrary.Jackson, PessoaFisicaWSVO.class)
		.log(LoggingLevel.INFO, log, "retorno: ${body}")
		.marshal().json(JsonLibrary.Jackson); 
    
		
	}

}

