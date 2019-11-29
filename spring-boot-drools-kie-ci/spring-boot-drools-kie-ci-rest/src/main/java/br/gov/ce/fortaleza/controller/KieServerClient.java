package br.gov.ce.fortaleza.controller;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.command.BatchExecutionCommand;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieContainerResource;
import org.kie.server.api.model.KieContainerResourceFilter;
import org.kie.server.api.model.KieContainerResourceList;
import org.kie.server.api.model.KieServerInfo;
import org.kie.server.api.model.KieServiceResponse.ResponseType;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.CaseServicesClient;
import org.kie.server.client.DMNServicesClient;
import org.kie.server.client.DocumentServicesClient;
import org.kie.server.client.JobServicesClient;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.QueryServicesClient;
import org.kie.server.client.RuleServicesClient;
import org.kie.server.client.SolverServicesClient;
import org.kie.server.client.UIServicesClient;
import org.kie.server.client.UserTaskServicesClient;

public class KieServerClient {

//	     REST API base URL, credentials, and marshalling format
	static String url = "https://localhost:8443/services/rest/server";
	static String username = "adminUser";
	static String password = "cotecdmAdmin2@l9";
	String session = "defaultStatelessKieSession";
	String containerId = "FolhaRegras-Refactoring";
	

	private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;

	private static KieServicesConfiguration conf;

	// KIE client for common operations
	private static KieServicesClient kieServicesClient;

	// Rules client
	private static RuleServicesClient ruleClient;
	private static KieCommands commandsFactory;

	// Process automation clients
	private static CaseServicesClient caseClient;
	private static DocumentServicesClient documentClient;
	private static JobServicesClient jobClient;
	private static ProcessServicesClient processClient;
	private static QueryServicesClient queryClient;
	private static UIServicesClient uiClient;
	private static UserTaskServicesClient userTaskClient;

	// DMN client
	private static DMNServicesClient dmnClient;

	// Planning client
	private static SolverServicesClient solverClient;

//	public static void main(String[] args) {
//		initializeKieServerClient();
//		initializeDroolsServiceClients();
//		initializeJbpmServiceClients();
//		initializeSolverServiceClients();
//	}

	public void listContainersWithFilter() {

		// Filter containers by releaseId "org.example:container:1.0.0.Final" and status
		// FAILED
		KieContainerResourceFilter filter = new KieContainerResourceFilter.Builder()
				.releaseId("br.gov.ce.fortaleza.sepog", "FolhaRegras-Refactoring", "1.0.0")
				// .status(KieContainerStatus.FAILED)
				.build();

		// Using previously created KieServicesClient
		KieContainerResourceList containersList = kieServicesClient.listContainers(filter).getResult();
		List<KieContainerResource> kieContainers = containersList.getContainers();

		System.out.println("Available containers: ");

		for (KieContainerResource container : kieContainers) {
			System.out.println("\t" + container.getContainerId() + " (" + container.getReleaseId() + ")");
		}
	}

	public void listCapabilities() {

		KieServerInfo serverInfo = kieServicesClient.getServerInfo().getResult();
		System.out.print("Server capabilities:");

		for (String capability : serverInfo.getCapabilities()) {
			System.out.print(" " + capability);
		}

		System.out.println();
	}

	public void listContainers() {
		KieContainerResourceList containersList = kieServicesClient.listContainers().getResult();
		List<KieContainerResource> kieContainers = containersList.getContainers();
		System.out.println("Available containers: ");
		for (KieContainerResource container : kieContainers) {
			System.out.println("\t" + container.getContainerId() + " (" + container.getReleaseId() + ")");
		}
	}

	public static void initializeKieServerClient() {
		conf = KieServicesFactory.newRestConfiguration(url, username, password);
		conf.setMarshallingFormat(FORMAT);
		conf.setUseSsl(true);
		kieServicesClient = KieServicesFactory.newKieServicesClient(conf);
	}

	public static void initializeDroolsServiceClients() {
		ruleClient = kieServicesClient.getServicesClient(RuleServicesClient.class);
		dmnClient = kieServicesClient.getServicesClient(DMNServicesClient.class);
	}

	public static void initializeJbpmServiceClients() {
		caseClient = kieServicesClient.getServicesClient(CaseServicesClient.class);
		documentClient = kieServicesClient.getServicesClient(DocumentServicesClient.class);
		jobClient = kieServicesClient.getServicesClient(JobServicesClient.class);
		processClient = kieServicesClient.getServicesClient(ProcessServicesClient.class);
		queryClient = kieServicesClient.getServicesClient(QueryServicesClient.class);
		uiClient = kieServicesClient.getServicesClient(UIServicesClient.class);
		userTaskClient = kieServicesClient.getServicesClient(UserTaskServicesClient.class);
	}

	public static void initializeSolverServiceClients() {
		solverClient = kieServicesClient.getServicesClient(SolverServicesClient.class);
	}
	
	public void executeCommands() {

		List<Command<?>> commands = new ArrayList<Command<?>>();
		commands.add( KieServices.Factory.get().getCommands().newAgendaGroupSetFocus("Penalidades"));
		commands.add((Command<?>) KieServices.Factory.get().getCommands().newFireAllRules());
		
		BatchExecutionCommand batchExecution1 = KieServices.Factory.get().getCommands().newBatchExecution(commands,
				session);
		
		ServiceResponse<ExecutionResults> executeResponse = ruleClient.executeCommandsWithResults(containerId,
				batchExecution1);
		
		 if(executeResponse.getType() == ResponseType.SUCCESS) {
	    	    System.out.println("Commands executed with success! Response: ");
	    	    System.out.println(executeResponse.getResult().toString());
	    	  } else {
	    	    System.out.println("Error executing rules. Message: ");
	    	    System.out.println(executeResponse.getMsg());
	    	  }
			 
	}

	public String executeRuleKieServer() {
		initializeKieServerClient();
		initializeDroolsServiceClients();
//		initializeJbpmServiceClients();
		initializeSolverServiceClients();
		listCapabilities();
		listContainers();
		listContainersWithFilter();
		executeCommands();
		return "ok";

	}
}
