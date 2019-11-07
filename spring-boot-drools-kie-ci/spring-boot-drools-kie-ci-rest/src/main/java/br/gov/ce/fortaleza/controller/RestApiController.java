package br.gov.ce.fortaleza.controller;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.cdi.KReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class RestApiController {

/* 	@Autowired
	PessoaFisicaWSVORepository pessoaFisicaWSVORepository; */
    

    // @KSession
    // @KReleaseId(groupId = "org.drools.workshop",artifactId = "my-first-drools-kjar", version = "1.0-SNAPSHOT")
	// private KieSession kSession;
	
	// public String executeRule(String numDocumento) {

	// 	kSession.insert("Hi There From Test!");
	// 	kSession.fireAllRules();
	// 	return "ok";

	// }
	public String executeRule(String numDocumento) {

		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.newKieContainer(
			ks.newReleaseId("org.drools.workshop", "my-first-drools-kjar", "1.0-SNAPSHOT")
		);

		KieScanner kScanner = ks.newKieScanner(kContainer);
		kScanner.start(10000L);
		KieSession kSession = kContainer.newKieSession();

		kSession.insert("Hi There From Test!");
		kSession.fireAllRules();

		return "ok";

	}
	

}
