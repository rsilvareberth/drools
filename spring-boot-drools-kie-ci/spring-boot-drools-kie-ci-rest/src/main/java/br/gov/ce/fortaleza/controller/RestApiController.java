package br.gov.ce.fortaleza.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kie.api.runtime.ExecutionResults;
import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.cdi.KReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieServiceResponse.ResponseType;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class RestApiController {


	static String url = "https://localhost:8090/services/rest/server";
	//http://localhost:8080/kie-server/services/rest/server
	//String url = "https://localhost:8443";
	//String url = "https://localhost:8443/services/rest/server/containers/instances";
	static String username = "adminUser";
	static String password = "cotecdmAdmin2@l9";
	String session = "defaultStatelessKieSession";
	String containerId = "FolhaRegras-Refactoring";
	
    private static KieServicesConfiguration conf;

    // KIE client for common operations
    private static KieServicesClient kieServicesClient;
    
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
    public static void initializeKieServerClient() {
        conf = KieServicesFactory.newRestConfiguration(url, username, password);
        conf.setMarshallingFormat(MarshallingFormat.XSTREAM);
        kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
    }
    
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

	
	public RuleServicesClient criarConexaoKieServer() {
		conf = KieServicesFactory.newRestConfiguration(url, username, password,120000);
		conf.setMarshallingFormat(MarshallingFormat.XSTREAM);
		conf.setUseSsl(true);
		KieServicesFactory.newKieServicesClient(conf);
		KieServicesClient client = KieServicesFactory.newKieServicesClient(conf);
		RuleServicesClient ruleClient = client.getServicesClient(RuleServicesClient.class);
		return ruleClient;
	}

	
	public String executeRuleKieServer(String numDocumento) {

		List<Command<?>> commands = new ArrayList<Command<?>>();

		commands.add( KieServices.Factory.get().getCommands().newAgendaGroupSetFocus("Hi There From Test!"));
		commands.add((Command<?>) KieServices.Factory.get().getCommands().newFireAllRules());
		
		RuleServicesClient ruleClient = criarConexaoKieServer();
		
		BatchExecutionCommand batchCommand = KieServices.Factory.get().getCommands().newBatchExecution(commands,
				session);
		
		ServiceResponse<ExecutionResults> response = ruleClient.executeCommandsWithResults(containerId, batchCommand);
		System.out.println(response.getResult().getValue("Funcionarios"));
//		kSession.insert("Hi There From Test!");
//		kSession.fireAllRules();

		return "ok";

	}
	
	

}
