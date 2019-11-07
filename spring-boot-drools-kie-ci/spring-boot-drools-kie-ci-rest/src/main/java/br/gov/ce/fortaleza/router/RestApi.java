package br.gov.ce.fortaleza.router;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class RestApi extends RouteBuilder {
	
	@Value("${context.api.version}")
	private String apiVersion;
	
    @Override
    public void configure() {
       
    rest(apiVersion.concat("/pessoa"))
    	.id("restRoute")
    	.get("{teste}")
	    	.id("teste")
	    	.param()
	    		.type(RestParamType.path)
	    		.name("teste")
	    	.endParam()
	    	.to("direct:search-person");
    }
}
