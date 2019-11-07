package br.gov.ce.fortaleza.config;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.springframework.stereotype.Component;

@Component
public class CamelContextManager implements CamelContextAware{
 
    protected CamelContext camelContext;
     
    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
 
    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }

	
     
 
}