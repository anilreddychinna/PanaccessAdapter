package com.obs.integrator.panacess;

import org.apache.log4j.Logger;

public class PanacessProcessCommandImpl {

	static Logger logger = Logger.getLogger("");
	
	public static String Authpin;
	
    private SendingDataToPanacessServer sendingDataToPanacessServer;

	public PanacessProcessCommandImpl() {
		
		sendingDataToPanacessServer=new SendingDataToPanacessServer();
		
	}

	public void processRequest(PanacessProcessRequestData processRequestData) {
		
		System.out.println("***********....Request Type ......... "+processRequestData.getRequestType());
		try {
				
				if (processRequestData.getRequestType().equalsIgnoreCase(PanacessConstants.REQ_CLIENT_ACTIVATION)) {
					  
					String output = sendingDataToPanacessServer.clientActivationCommandProcessing(processRequestData);
					
					if(!output.isEmpty()){
						PanacessProcessCommandImpl.process(output, processRequestData.getId(),processRequestData.getPrdetailsId(), processRequestData.getClientId(), processRequestData.getRequestType());	
					}

				} else if (processRequestData.getRequestType().equalsIgnoreCase(PanacessConstants.REQ_ACTIVATION)) {
					  
				String output = sendingDataToPanacessServer.activationCommandProcessing(processRequestData);
					if(!output.isEmpty()){
						PanacessProcessCommandImpl.process(output, processRequestData.getId(),processRequestData.getPrdetailsId(), processRequestData.getClientId(), processRequestData.getRequestType());	
					}

				} else if (processRequestData.getRequestType().equalsIgnoreCase(PanacessConstants.REQ_RECONNECTION) ||
						processRequestData.getRequestType().equalsIgnoreCase(PanacessConstants.REQ_RENEWAL_AE)) {
					
				//	String output = sendingDataToPanacessServer.reconnectionCommandProcessing(processRequestData);
					String output =null;
					if(!output.isEmpty()){
						
						PanacessProcessCommandImpl.process(output, processRequestData.getId(),processRequestData.getPrdetailsId(), processRequestData.getClientId(), processRequestData.getRequestType());	
						
					}
					
				} else if (processRequestData.getRequestType().equalsIgnoreCase(PanacessConstants.REQ_DISCONNECTION)) {
					
				  String output = sendingDataToPanacessServer.disConnectionCommandProcessing(processRequestData);
					if(!output.isEmpty()){
						PanacessProcessCommandImpl.process(output, processRequestData.getId(),processRequestData.getPrdetailsId(), processRequestData.getClientId(), processRequestData.getRequestType());	
						
					}
					
				} else if (processRequestData.getRequestType().equalsIgnoreCase(PanacessConstants.REQ_CHANGE_PLAN)) {
					
				//	String output = sendingDataToPanacessServer.changePlanCommandProcessing(processRequestData);
					String output =null;
					if(!output.isEmpty()){
						
						PanacessProcessCommandImpl.process(output, processRequestData.getId(),processRequestData.getPrdetailsId(), processRequestData.getClientId(), processRequestData.getRequestType());	
						
					}
					
				} else if (processRequestData.getRequestType().equalsIgnoreCase(PanacessConstants.REQ_TERMINATE)) {
					
					//String output = sendingDataToPanacessServer.TerminateCommandProcessing(processRequestData);
					String output =null;
					if(!output.isEmpty()){
						
						PanacessProcessCommandImpl.process(output, processRequestData.getId(),processRequestData.getPrdetailsId(), processRequestData.getClientId(), processRequestData.getRequestType());	
						
					}
					
				} else if (processRequestData.getRequestType().equalsIgnoreCase(PanacessConstants.REQ_DEVICE_SWAP) ) {
					
				// String output = sendingDataToPanacessServer.deviceSwapCommandProcessing(processRequestData);
					String output =null;
					if(!output.isEmpty()){
						
						PanacessProcessCommandImpl.process(output, processRequestData.getId(),processRequestData.getPrdetailsId(), processRequestData.getClientId(), processRequestData.getRequestType());	
						
					}

				} else if (processRequestData.getRequestType().equalsIgnoreCase(PanacessConstants.REQ_RELEASE_DEVICE) ) {
					
				// String output = sendingDataToPanacessServer.releaseDeviceCommandProcessing(processRequestData);
					String output =null;
					if(!output.isEmpty()){
						
						PanacessProcessCommandImpl.process(output, processRequestData.getId(),processRequestData.getPrdetailsId(), processRequestData.getClientId(), processRequestData.getRequestType());	
						
					}

				} else if (processRequestData.getRequestType().equalsIgnoreCase(PanacessConstants.REQ_MESSAGE) ) {
					
				//	String output = sendingDataToPanacessServer.beeniusOSDMessageCommandProcessing(processRequestData);
					String output =null;
					if(!output.isEmpty()){
						
						PanacessProcessCommandImpl.process(output, processRequestData.getId(),processRequestData.getPrdetailsId(), processRequestData.getClientId(), processRequestData.getRequestType());	
						
					}

				} else if (processRequestData.getRequestType().equalsIgnoreCase(PanacessConstants.REQ_RENEWAL_BE) ) {
					
					String output = PanacessConstants.SUCCESS;
					
					if(!output.isEmpty()){
						
						PanacessProcessCommandImpl.process(output, processRequestData.getId(),processRequestData.getPrdetailsId(), processRequestData.getClientId(), processRequestData.getRequestType());	
						
					}

				} else  {
					logger.error("Request Type Not Found : "+ processRequestData.getRequestType());
					String output = PanacessConstants.FAILURE + "Invalid Request Type";
					PanacessProcessCommandImpl.process(output, processRequestData.getId(),processRequestData.getPrdetailsId(), processRequestData.getClientId(), processRequestData.getRequestType());	
					
				}
			
		} catch (Exception e) {
			logger.error("Exception : " + e.getMessage());
		} 

	}


	
	
	public static void process(String value, Long id, Long prdetailsId, Long clientId, String requestType){	
		try{		
			logger.info("output from CAS Server is :" +value);
			if(value==null){
				throw new NullPointerException();
			}else{		
				PanacessConsumer.sendResponse(value,id,prdetailsId, clientId, Authpin, requestType);
			}		
		} catch(NullPointerException e){
			logger.error("NullPointerException : Output from the Oss System Server is : " + value);
		} catch (Exception e) {
		    logger.error("Exception : " + e.getMessage());
	    }
		
	}
	
	
}
