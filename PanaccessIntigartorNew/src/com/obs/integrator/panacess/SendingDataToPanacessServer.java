package com.obs.integrator.panacess;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Calendar;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.panaccess.cableview.wsdl.v4_0.operator.CableViewService;
import de.panaccess.cableview.wsdl.v4_0.operator.CableViewServiceLocator;
import de.panaccess.cableview.wsdl.v4_0.operator.CableViewServicePortType;
import de.panaccess.cableview.wsdl.v4_0.operator.ProductEntry;
import de.panaccess.cableview.wsdl.v4_0.operator.ProductEntryList;
import de.panaccess.cableview.wsdl.v4_0.operator.Smartcard;
import de.panaccess.cableview.wsdl.v4_0.operator.SmartcardInfo;
import de.panaccess.cableview.wsdl.v4_0.operator.Subscriber;
import de.panaccess.cableview.wsdl.v4_0.operator.Subscription;

public class SendingDataToPanacessServer {

	//private BeesmartAdminBean beesmartAdminBean;
	static Logger logger = Logger.getLogger("");
	String sessionId=null;
	CableViewServicePortType cvService;
	public SendingDataToPanacessServer()  {
		String endpoint = "https://cv01.panaccess.com/index.php?requestMode=wsdl&v=3.0&r=operator&wsdl";
		String userName = "demo";
		String password = "demo2010";
		CableViewService service = new CableViewServiceLocator();
		try {
		   	cvService = service.getCableViewServicePort(new URL(endpoint));
			 sessionId = cvService.cvLogin(userName, password);
			if (sessionId != null) {
				logger.info("Panacess server connected Successfully ... ");
			} else {			
				System.exit(0);	
			}
		} catch (MalformedURLException e) {
			logger.info("Panacess server Stoped ... "+e.getMessage()+"..."+e.getCause());
			e.printStackTrace();
		} catch (ServiceException e) {
			logger.info("Panacess server Stoped ... "+e.getMessage()+"..."+e.getCause());
			e.printStackTrace();
		} catch (RemoteException e) {
			logger.info("Panacess server Stoped ... "+e.getMessage()+"..."+e.getCause());
			e.printStackTrace();
		}
	}

	// Adding Subscriber in panaccess Portal
	public String clientActivationCommandProcessing(PanacessProcessRequestData processRequestData) throws JSONException, RemoteException {
		logger.info("Add Subscriber in Panacess Server... Processing...");
		
		String result, SubscriberResult = null;
		try {
			Subscriber sc=new Subscriber();
			/*sc.setTechnicalNotes(" test notes");
			//sc.setCode("13");
			sc.setHcId("hc01220001");
			sc.setFirstName("test1");
			sc.setLastName("test");
			sc.setComment("comment notes");
			sc.setCountryCode("AF");*/
			sc.setTechnicalNotes(" test notes");
			//sc.setCode("13");
			sc.setHcId("hc01220001");
			sc.setFirstName(processRequestData.getFirstName());
			sc.setLastName(processRequestData.getLastName());
			sc.setComment("OBS-"+processRequestData.getAccountNo());
			sc.setCountryCode("AF");
			//sc.setCountryCode("IN");
			//sc.setCountryCode(processRequestData.getRegionCode());
			//cvGetSubscriberResponse 
			// Contact c =new Contact();
			//c.setContact("anil.chinna147@gmail.com");
			cvService.cvAddSubscriber(sessionId, sc);
			
			logger.info("Adding Subscriber in panaccess Server  ... Sucessfully ");
			
			
			//sc= cvService.cvGetSubscriber(sessionId, processRequestData.getAccountNo());
			logger.info("Subscriber Details .clientId ..."+processRequestData.getAccountNo()+"...Code..."+sc.getCode()+" ....Name..."+sc.getFirstName());
			result =  PanacessConstants.SUCCESS;
			
			logger.info("Adding Subscriber in panaccess Server  ... Sucessfully  finally ..........");
		}catch (Exception e) {
			logger.info("Adding Subscriber in Panacces Failed ... "+e.getMessage()+".. ...due to......"+e.getLocalizedMessage());
			result =  PanacessConstants.FAILURE + " Due to "+e.getMessage();
		}
		return result;
	}
	
	// Preparing Device Adding in Beenius Portal
	/*private WstoDevice addDeviceInBeeniusPortal(PanacessProcessRequestData processRequestData) {

		Date d = new Date();
		SimpleDateFormat formater = new SimpleDateFormat(PanacessConstants.DATE_PATTERN);
		String date = formater.format(d);
		String DeviceUid = processRequestData.getHardwareId().replaceAll(":","");

		WstoDevice wstoDevice = new WstoDevice();

		wstoDevice.setDeviceProvisioningDate(date);
		wstoDevice.setDeviceTypeDesc(processRequestData.getItemDescription());
		wstoDevice.setDeviceUid(DeviceUid.toLowerCase());

		wstoDevice.setDisplayTypeDesc(processRequestData.getItemDescription());
		wstoDevice.setRegionName(processRequestData.getRegionName());
		wstoDevice.setVideoTypeDesc(PanacessConstants.VIDEO_TYPE_DESC);
		wstoDevice.setSubscriberUid(processRequestData.getAccountNo());

		return wstoDevice;
	}*/
	
	//Binding Subscriber/Client with Device
	/*private String deviceAndSubscriberBinding(String operatorUid, String deviceUid, String subscriberUid){
		
		String output;
		
		Head head = beesmartAdminBean.connectDeviceToSubscriber(operatorUid, deviceUid, subscriberUid);
		
		logger.info("Output From connectDeviceToSubscriber: " + head.getDescription());
		
		if(head.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)){
			
			logger.info("Device Added Successfully ... ");
			
			output = PanacessConstants.SUCCESS;
			
		}else{
			
			logger.info("connectDeviceToSubscriber Operation Failed, deviceUid: "
					+ deviceUid + " and SubscriberUid: " + subscriberUid);
			
			output = PanacessConstants.FAILURE + head.getDescription();
		}
		
		return output;
	}
	*/
	//device actions Processing
	private String deviceCheckAndProcessing(PanacessProcessRequestData processRequestData) throws RemoteException, JSONException {
		
		String endpoint = "https://cv01.panaccess.com/index.php?requestMode=wsdl&v=3.0&r=operator&wsdl";
		String userName = "demo";
		String password = "demo2010";
		CableViewService service = new CableViewServiceLocator();
		try {
		   	cvService = service.getCableViewServicePort(new URL(endpoint));
			 sessionId = cvService.cvLogin(userName, password);
			if (sessionId != null) {
				logger.info("Panacess server connected Successfully ... ");
			} else {			
				System.exit(0);	
			}
		} catch (Exception e) {
			System.out.println(" Exception occure in device checking process..........."+e.getMessage());
			
		}
		logger.info("********....Device checkking process  Started here....*******");
		//verify the device existed or not 
		String deviceResponse=null;
		
		final String operatorUid = processRequestData.getOfficeUId();
		
		if(null == processRequestData.getHardwareId()){
			return PanacessConstants.FAILURE + " Hardware Id Null";
		}
		final String deviceUid = processRequestData.getHardwareId().replaceAll(":", "");
		final String accountId = processRequestData.getAccountNo();
		
		logger.info("********...Subscriber Details here ...."+accountId+"...*******");
		Subscriber s=new Subscriber();
		s=cvService.cvGetSubscriber(sessionId, "45");
		//s=cvService.cvGetSubscriber(sessionId, accountId);
		logger.info("********...Subscriber Details  ...."+s.getCode()+"..."+s.getFirstName()+"..." +
				""+s.getSupervisor()+"...."+s.getHcId()+"....*******");
		
		SmartcardInfo smart=new SmartcardInfo();
		//smart = cvService.cvGetSmartcardInfo(deviceUid, processRequestData.getHardwareId());
		
		Smartcard[] sqw=cvService.cvGetSmartcardsOfSubscriber(sessionId, "45");
		for (Smartcard entry : sqw) {
			deviceResponse=entry.getSn();
			System.out.println("------.....Getting Subscriber  Smart card are .....---"+entry.getSn()+".....IDs..."+entry.getChecksum());
		}
		if(sqw.length == 0){
			logger.info(" *********....Smart card not found for this subscriber .....***");
			logger.info(" *********....Adding Smart card no .."+deviceUid+"....to subscriber .....***");
			cvService.cvAddSmartcardToSubscriber(sessionId, deviceUid, "45");
			logger.info(" *********....Adding Smart card SucessFully ......"+deviceUid+"....to subscriber .....***");
			deviceResponse= deviceUid;
		}
		
		/*Smartcard sm= new Smartcard();
		cvService.cvAddSmartcardToSubscriber(sessionId, "5465464", "45");*/
		
		//
		// WstoDeviceResponse wstoDeviceResponse = beesmartAdminBean.getDevice(operatorUid, deviceUid);
		
	//	String wstoResponse = wstoDeviceResponse.getHead().getDescription();
		/*logger.info("wstoResponse in Device checking="+wstoResponse);
		if(wstoResponse.equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)){
			
		//	String subscriberUid = wstoDeviceResponse.getData().getSubscriberUid();
			
			if(subscriberUid == null){		
			//	deviceResponse = deviceAndSubscriberBinding(operatorUid, deviceUid, accountId);
				
			} else if(subscriberUid.equalsIgnoreCase(accountId)){
				deviceResponse = PanacessConstants.SUCCESS;
			
			} else{
				String message = "Device Binding Failed, Because this " + processRequestData.getHardwareId() + 
						" is already assigned to this Subscriber. Subscriber_uid=" + subscriberUid;
				logger.error(message);
				deviceResponse = PanacessConstants.FAILURE + message;
			}
			
		}else if(wstoResponse.contains(PanacessConstants.REQUIRED_DEVICE_NOT_FOUND)){
			WstoDevice wstoDevice = addDeviceInBeeniusPortal(processRequestData);
			Head head = beesmartAdminBean.addDevice(processRequestData.getOfficeUId(), wstoDevice);

			logger.info("Output From Device Addition: " + head.getDescription());

			if (head.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {

				logger.info("Device Added Successfully to Subscriber and Beenius Portal ... ");

				deviceResponse = PanacessConstants.SUCCESS;

			}else{
				
				logger.info("Device Add to Beenius Portal Failed");
				
				deviceResponse = PanacessConstants.FAILURE + head.getDescription();
			}
			
			
		}else{
			
			logger.info("Device Assigned to Subscriber Failed, deviceUid: " + deviceUid + " and SubscriberUid: " + accountId);

			deviceResponse = PanacessConstants.FAILURE + wstoResponse;
			
		}*/
		
		/*return deviceResponse;*/
		return deviceResponse;

	}
	
	//add channels/services and device in Beenius Server
	public String activationCommandProcessing(PanacessProcessRequestData processRequestData) throws RemoteException {

		
		
		String output=null, chPackageResult = null, status = null, statusChangeResult, deviceOutput;
		   String  statusvalue = null;
		   
		try {
			logger.info("********....Enter Activation  order ......********");
			
			int offset = 0;
			int limit = 100;
			ProductEntryList list;
		      list =  cvService.cvGetListOfProducts(sessionId, offset, limit, "name", "asc", null);
				for (ProductEntry entry : list.getProductEntries()) {
					System.out.println(" .......List of products .........Id...."+entry.getProductId()+"..........Name ... "+entry.getName());
				}
			
			/**
			 * For List Of Subscribers 
			 * 
			 * SubscriberEntryList sublist;
			sublist=cvService.cvGetListOfSubscribers(sessionId, "live", offset, limit, "name", "asc", null);
	     	for (SubscriberEntry entry : sublist.getSubscriberEntries()) {
				System.out.println(" List of Subscribers ...."+entry.getCode()+"......"+entry.getHcId()+": "+entry.getSmartcards());
			}*/
				
	        JSONObject obj = new JSONObject(processRequestData.getProduct());
		   String planId= obj.getString("planIdentification");
		   logger.info("*********....Plan id is.........."+planId);
			
		   deviceOutput = deviceCheckAndProcessing(processRequestData);
			logger.info("*******....Devices checking Responce is............ "+deviceOutput);
			logger.info("*******.....Add SubscriberChPackage To Subscriber ... ");
			
			String[] smartcards= new String[1];
			 smartcards[0]=deviceOutput;
			// Calendar StartDate1 = Calendar.getInstance();  
			// Calendar EndDate1 = Calendar.getInstance();
			 Calendar StartDate = Calendar.getInstance();  
			 Calendar EndDate = Calendar.getInstance(); 
			 EndDate.add(Calendar.MONTH, 1); 
			 logger.info("The Start Date is ....." + StartDate.getTime());  
			 logger.info("The End Date is ....." + EndDate.getTime()); 
			 
			 logger.info("Add Order to subscriber ........Paln Id "+Integer.parseInt(planId)+"...Card ."+smartcards+"......."); 
			/* int resultId=cvService.cvAddFlexibleOrderToSubscriber
					   (sessionId, Integer.parseInt(planId), "45", 
							   StartDate, EndDate, false, smartcards);*/
			 
			 int resultId=cvService.cvAddFlexibleOrderToSubscriber
					 (sessionId, Integer.parseInt(planId), "45", StartDate, EndDate, false, smartcards, null);
			 
			 
			 
				
			 logger.info("SubscriberChPackage Added Successfully with the " +
						"Product  : "+Integer.parseInt(planId)+"..." + resultId);
			 
			 logger.info("*****.....Added Successfully Result is ......Order ID ...." + resultId+"........*******");
			
			 statusvalue=PanacessConstants.RES_SUCCESSFUL;
			/*String product = processRequestData.getProduct();

			product = product.replace("\"{", "{");
			product = product.replace("}\"", "}");
			product = product.replace("\\", "");

			JSONObject object = new JSONObject(product);

			JSONArray array = object.getJSONArray(PanacessConstants.SERVICES);*/

			/*for (int i = 0; i < array.length(); i++) {

				JSONObject subObject = new JSONObject(array.getJSONObject(i).toString());

				String serviceIdentification = subObject.getString(PanacessConstants.SERVICEIDENTIFICATION);

				Head head = beesmartAdminBean.addSubscriberChPackage(processRequestData.getOfficeUId(),
						processRequestData.getAccountNo(), serviceIdentification);

				logger.info("Output From SubscriberChPackage : " + head.getDescription());

				if (head.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {

					logger.info("SubscriberChPackage Added Successfully with the Service : " + serviceIdentification);

					status = PanacessConstants.ACTIVE;

					chPackageResult = PanacessConstants.SUCCESS;

				} else {

					logger.info("Adding SubscriberChPackage Failed with the Service : " + serviceIdentification);

					status = PanacessConstants.INACTIVE;

					chPackageResult = PanacessConstants.FAILURE + head.getDescription();

					break;

				}
			}*/
		//	statusChangeResult = subscriberStatusChange(processRequestData, status);
			 
			 if (statusvalue.equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {
					status = PanacessConstants.ACTIVE;
					chPackageResult = PanacessConstants.SUCCESS;
					
					output = "Services/Channels Adding Result:" + chPackageResult + ", Customer Account Status:" + chPackageResult
							+ "Order ID " + resultId;

				}

			 return output;
		} catch (Exception  e) {
			
			logger.info("Adding SubscriberChPackage Failed with the Service :");
			status = PanacessConstants.INACTIVE;
			chPackageResult = PanacessConstants.FAILURE + e.getMessage();
			
		    return chPackageResult;
		}
	}
	
	// Subscriber Create Data Preparing
	/*private WstoSubscriber addSubscriberCommandProcessing(PanacessProcessRequestData processRequestData, String status)
				throws JSONException {

		WstoDefaultUser wstoDefaultUser = new WstoDefaultUser();

		wstoDefaultUser.setDefaultUserName(processRequestData.getLastName());
		wstoDefaultUser.setDefaultUserPin(PanacessConstants.DEFAULT_USER_PIN);

		WstoSubscriber wstoSubscriber = new WstoSubscriber();

		wstoSubscriber.setDefaultUser(wstoDefaultUser);
		wstoSubscriber.setOperator(processRequestData.getBranch());
		wstoSubscriber.setRegionName(processRequestData.getRegionName());
		wstoSubscriber.setSubscriberAccessibility(PanacessConstants.TRUE);
		wstoSubscriber.setSubscriberName(processRequestData.getFirstName());
		wstoSubscriber.setSubscriberStatusDesc(status);
		wstoSubscriber.setSubscriberSurname(processRequestData.getLastName());
		wstoSubscriber.setSubscriberUid(processRequestData.getAccountNo());
		wstoSubscriber.setChSubscriberPackageName(PanacessConstants.CH_SUBSCRIBER_PACKAGENAME);
		wstoSubscriber.setSubscriberPackageName(PanacessConstants.CH_SUBSCRIBER_PACKAGENAME);
		wstoSubscriber.setVodSubscriberPackageName(PanacessConstants.VOD_SUBSCRIBER_PACKAGENAME);

		return wstoSubscriber;

	}*/
	
    // Reconnection or DisConnection Sending to Beenius
	/*public String subscriberStatusChange(PanacessProcessRequestData processRequestData, String status) throws JSONException {
	
		WstoSubscriber wstoSubscriber = addSubscriberCommandProcessing(processRequestData, status);
		
		Head response=beesmartAdminBean.setSubscriber(processRequestData.getOfficeUId(), wstoSubscriber);
	
		logger.info("Output From Subscriber : "+response.getDescription());
		
		if(response.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)){
			
			return PanacessConstants.SUCCESS;
			
		} else {
			
			return PanacessConstants.FAILURE + response.getDescription();
			
		}
	}*/
	
	//Reconnection Data Preparing
	/*public String reconnectionCommandProcessing(PanacessProcessRequestData processRequestData) throws JSONException {

		logger.info("Reconnection SubscriberChPackage... ");

		String ChPackageResult = null;

		String product = processRequestData.getProduct();
		product = product.replace("\"{", "{");
		product = product.replace("}\"", "}");
		product = product.replace("\\", "");

		JSONObject object = new JSONObject(product);

		JSONArray array = object.getJSONArray(PanacessConstants.SERVICES);
		
		String successdCommands[] = new String[array.length()];

		for (int i = 0; i < array.length(); i++) {

			JSONObject subObject = new JSONObject(array.getJSONObject(i).toString());

			String serviceIdentification = subObject.getString(PanacessConstants.SERVICEIDENTIFICATION);

			Head head = beesmartAdminBean.addSubscriberChPackage(processRequestData.getOfficeUId(),
					processRequestData.getAccountNo(), serviceIdentification);

			logger.info("Output From SubscriberChPackage : " + head.getDescription() + " , Service : " + serviceIdentification);

			if (head.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {

				logger.info("SubscriberChPackage Added Successfully with the Service : " + serviceIdentification);

				ChPackageResult = PanacessConstants.SUCCESS;
				
				successdCommands[i] = serviceIdentification;

			} else {

				logger.info("Adding SubscriberChPackage Failed with the Service : " + serviceIdentification);

				ChPackageResult = PanacessConstants.FAILURE + head.getDescription();

				reDeleteChPackages(processRequestData, successdCommands);
				
				break;

			}
		}

		if (ChPackageResult.equalsIgnoreCase(PanacessConstants.SUCCESS)) {

			ChPackageResult = subscriberStatusChange(processRequestData, PanacessConstants.ACTIVE);
		}

		return ChPackageResult;
	}*/
		
	/*private void reDeleteChPackages(PanacessProcessRequestData processRequestData, String[] successdCommands) throws JSONException {

		
		for(int i=0;i<successdCommands.length;i++){
			
			String serviceIdentification = successdCommands[i];
			
			Head head = beesmartAdminBean.deleteSubscriberChPackage(processRequestData.getOfficeUId(),
					processRequestData.getAccountNo(), serviceIdentification);

			logger.info("Output From SubscriberChPackage : " + head.getDescription() + " , Service : " + serviceIdentification);

			if (head.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {

				logger.info(" Already added SubscriberChPackage Deleted Successfully with the Service : " + serviceIdentification);
				
			} else {

				logger.info("Already added SubscriberChPackage Deletion Failed with the Service : " + serviceIdentification);
				
				subscriberStatusChange(processRequestData,PanacessConstants.INACTIVE);	
				
				logger.info("Subscriber Status changed to INACTIVE.....");
				
				return;

			}
		}	
		
	}*/

	/*private void reAssignChPackages(PanacessProcessRequestData processRequestData, String[] successdCommands) throws JSONException {
		
		for(int i=0;i<successdCommands.length;i++){
			
			String serviceIdentification = successdCommands[i];
			
			Head head = beesmartAdminBean.addSubscriberChPackage(processRequestData.getOfficeUId(),
					processRequestData.getAccountNo(), serviceIdentification);

			logger.info("Output From SubscriberChPackage : " + head.getDescription() + " , Service : " + serviceIdentification);

			if (head.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {

				logger.info("SubscriberChPackage Re-Added Successfully with the Service : " + serviceIdentification);
				
			} else {

				logger.info("Re-Adding SubscriberChPackage Failed with the Service : " + serviceIdentification);
				
				break;

			}
		}
		
		subscriberStatusChange(processRequestData,PanacessConstants.INACTIVE);		
		
		logger.info("Subscriber Status changed to INACTIVE.....");
	}*/

		//  DisConnection Data Preparing
	public String disConnectionCommandProcessing(PanacessProcessRequestData processRequestData) throws JSONException {

		logger.info("***********.......DisConnect SubscriberChPackage...*****");
		String ChPackageResult = null,result=null;
		
		String product = processRequestData.getProduct();
		product = product.replace("\"{", "{");
		product = product.replace("}\"", "}");
		product = product.replace("\\", "");
		JSONObject object = new JSONObject(product);
		
		JSONObject obj = new JSONObject(processRequestData.getProduct());
		   String planId= obj.getString("planIdentification");
		   System.out.println("******.... Plan id is............"+planId);
		  
		   Subscription [] sub;
		   try {
			sub=cvService.cvGetOrdersOfSubscriber(sessionId, "13", true);
			//Smartcard[] sqw=cvService.cvAddSmartcardToSubscriber(result, smartcardId, code)
			
			for (Subscription entry : sub) {
				logger.info("***......Getting orders..."+entry.getOrderId()+" ............"+entry.getSubscriberCode()+"......"+entry.getRuntimeId());
			}
			logger.info("**....... List of orders....................."+sub);
	     	/*for (SubscriberEntry entry : sublist.getSubscriberEntries()) {
				System.out.println(" subscribers ...."+entry.getCode()+"......"+entry.getHcId()+": "+entry.getSmartcards());
			}*/
			
			
			cvService.cvDisableOrderOfSubscriber(sessionId, 878669, "13");
			logger.info("**......Desable Order Sucessfully ............");
		 
			JSONArray array = object.getJSONArray(PanacessConstants.SERVICES);
		     String successdCommands[] = new String[array.length()];
		
		 result="Sucessfull";
		
		
		   } catch (RemoteException e) {
			    result="fail"+e.getMessage();
			}
		   
		if (result.equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {

			logger.info("SubscriberChPackage Deleted Successfully with the Service : ");
			ChPackageResult = PanacessConstants.SUCCESS;
			//successdCommands[i] = serviceIdentification;

		} else {

			logger.info("Deleting SubscriberChPackage Failed with the Service : "+result);

			ChPackageResult = PanacessConstants.FAILURE;
			//reAssignChPackages(processRequestData,successdCommands);
		}
	
		/*for (int i = 0; i < array.length(); i++) {

			JSONObject subObject = new JSONObject(array.getJSONObject(i).toString());

			String serviceIdentification = subObject.getString(PanacessConstants.SERVICEIDENTIFICATION);

			Head head = beesmartAdminBean.deleteSubscriberChPackage(processRequestData.getOfficeUId(),
					processRequestData.getAccountNo(), serviceIdentification);

			logger.info("Output From DisConnect SubscriberChPackage : " + head.getDescription());

			if (head.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {

				logger.info("SubscriberChPackage Deleted Successfully with the Service : " + serviceIdentification);

				ChPackageResult = PanacessConstants.SUCCESS;
				
				successdCommands[i] = serviceIdentification;

			} else {

				logger.info("Deleting SubscriberChPackage Failed with the Service : " + serviceIdentification);

				ChPackageResult = PanacessConstants.FAILURE + head.getDescription();
				
				reAssignChPackages(processRequestData,successdCommands);
				
				break;

			}
		}*/

		if (ChPackageResult.equalsIgnoreCase(PanacessConstants.SUCCESS)) {

			logger.info(".........Sucessfully  desable ......");
			/*WstoAllChPackagesUidsForSubscriberResponse head2 = beesmartAdminBean.getAllChPackagesUidsForSubscriber(
					processRequestData.getOfficeUId(),processRequestData.getAccountNo());

			if (head2.getData().isEmpty()) {
				ChPackageResult = subscriberStatusChange(processRequestData,PanacessConstants.INACTIVE);
			}*/
		}

		return ChPackageResult;
	}
	
	
	
	//Change Plan Data Preparing and sending to Beenius
	/*public String changePlanCommandProcessing(PanacessProcessRequestData processRequestData) {

		logger.info("Adding SubscriberChPackage To Subscriber ... ");
		String OldChPackageResult = null, NewChPackageResult = null, Result = null;

		try {
			String product = processRequestData.getProduct();
			product = product.replace("\"{", "{");
			product = product.replace("}\"", "}");
			product = product.replace("\\", "");

			JSONObject object = new JSONObject(product);

			JSONArray oldarray = object.getJSONArray(PanacessConstants.OLDSERVICES);

			String oldArraryCommands[] = new String[oldarray.length()];

			for (int i = 0; i < oldarray.length(); i++) {

				JSONObject subObject = new JSONObject(oldarray.getJSONObject(i).toString());

				String serviceIdentification = subObject.getString(PanacessConstants.OLDSERVICEIDENTIFICATION);

				Head head = beesmartAdminBean.deleteSubscriberChPackage(processRequestData.getOfficeUId(),
						processRequestData.getAccountNo(), serviceIdentification);

				logger.info("Output From SubscriberChPackage : " + head.getDescription());

				if (head.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {

					logger.info("SubscriberChPackage Deleted Successfully with the Service : " + serviceIdentification);

					OldChPackageResult = PanacessConstants.SUCCESS;

					oldArraryCommands[i] = serviceIdentification;

				} else {

					logger.info("Deleting SubscriberChPackage Failed with the Service : " + serviceIdentification);

					OldChPackageResult = PanacessConstants.FAILURE + head.getDescription();

					reAssignChPackages(processRequestData, oldArraryCommands);

					break;

				}
			}

			if (OldChPackageResult != null && OldChPackageResult.equalsIgnoreCase(PanacessConstants.SUCCESS)) {

				JSONArray array = object.getJSONArray(PanacessConstants.SERVICES);
				
				String arrayCommands[] = new String[array.length()];
				
				for (int i = 0; i < array.length(); i++) {

					JSONObject subObject = new JSONObject(array.getJSONObject(i).toString());

					String serviceIdentification = subObject.getString(PanacessConstants.SERVICEIDENTIFICATION);

					Head head = beesmartAdminBean.addSubscriberChPackage(processRequestData.getOfficeUId(),
							processRequestData.getAccountNo(), serviceIdentification);



					logger.info("Adding SubscriberChPackage To Subscriber ... ");
					String OldChPackageResult = null, NewChPackageResult = null, Result = null;

					try {
						String product = processRequestData.getProduct();
						product = product.replace("\"{", "{");
						product = product.replace("}\"", "}");
						product = product.replace("\\", "");

						JSONObject object = new JSONObject(product);

						JSONArray oldarray = object.getJSONArray(PanacessConstants.OLDSERVICES);

						String oldArraryCommands[] = new String[oldarray.length()];

						for (int i = 0; i < oldarray.length(); i++) {

							JSONObject subObject = new JSONObject(oldarray.getJSONObject(i).toString());

							String serviceIdentification = subObject.getString(PanacessConstants.OLDSERVICEIDENTIFICATION);

							Head head = beesmartAdminBean.deleteSubscriberChPackage(processRequestData.getOfficeUId(),
									processRequestData.getAccountNo(), serviceIdentification);

							logger.info("Output From SubscriberChPackage : " + head.getDescription());

							if (head.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {

								logger.info("SubscriberChPackage Deleted Successfully with the Service : " + serviceIdentification);

								OldChPackageResult = PanacessConstants.SUCCESS;

								oldArraryCommands[i] = serviceIdentification;

							} else {

								logger.info("Deleting SubscriberChPackage Failed with the Service : " + serviceIdentification);

								OldChPackageResult = PanacessConstants.FAILURE + head.getDescription();

								reAssignChPackages(processRequestData, oldArraryCommands);

								break;

							}
						}

						if (OldChPackageResult != null && OldChPackageResult.equalsIgnoreCase(PanacessConstants.SUCCESS)) {

							JSONArray array = object.getJSONArray(PanacessConstants.SERVICES);
							
							String arrayCommands[] = new String[array.length()];
							
							for (int i = 0; i < array.length(); i++) {

								JSONObject subObject = new JSONObject(array.getJSONObject(i).toString());

								String serviceIdentification = subObject.getString(PanacessConstants.SERVICEIDENTIFICATION);

								Head head = beesmartAdminBean.addSubscriberChPackage(processRequestData.getOfficeUId(),
										processRequestData.getAccountNo(), serviceIdentification);

								logger.info("Output From SubscriberChPackage : " + head.getDescription() + " , Service : " + serviceIdentification);

								if (head.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {

									logger.info("SubscriberChPackage Added Successfully with the Service : " + serviceIdentification);

									arrayCommands [i] = serviceIdentification;
									
									NewChPackageResult = PanacessConstants.SUCCESS;

								} else {

									logger.info("Adding SubscriberChPackage Failed with the Service : " + serviceIdentification);

									NewChPackageResult = PanacessConstants.FAILURE + head.getDescription();
									
									reAssignChPackages(processRequestData, oldArraryCommands);
									
									reDeleteChPackages(processRequestData, arrayCommands);
									
									break;

								}
							}

						}

						if (NewChPackageResult != null && OldChPackageResult != null) {
							if (NewChPackageResult.equalsIgnoreCase(PanacessConstants.SUCCESS) && 
									OldChPackageResult.equalsIgnoreCase(PanacessConstants.SUCCESS)) {

								Result = PanacessConstants.SUCCESS;

							} else {
								Result = "NewChPackageResult: " + NewChPackageResult + " , OldChPackageResult: " + OldChPackageResult;
							}
						}

					} catch (JSONException e) {

						return e.getMessage();
					}

					return Result;

								logger.info("Output From SubscriberChPackage : " + head.getDescription() + " , Service : " + serviceIdentification);

					if (head.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {

						logger.info("SubscriberChPackage Added Successfully with the Service : " + serviceIdentification);

						arrayCommands [i] = serviceIdentification;
						
						NewChPackageResult = PanacessConstants.SUCCESS;

					} else {

						logger.info("Adding SubscriberChPackage Failed with the Service : " + serviceIdentification);

						NewChPackageResult = PanacessConstants.FAILURE + head.getDescription();
						
						reAssignChPackages(processRequestData, oldArraryCommands);
						
						reDeleteChPackages(processRequestData, arrayCommands);
						
						break;

					}
				}

			}

			if (NewChPackageResult != null && OldChPackageResult != null) {
				if (NewChPackageResult.equalsIgnoreCase(PanacessConstants.SUCCESS) && 
						OldChPackageResult.equalsIgnoreCase(PanacessConstants.SUCCESS)) {

					Result = PanacessConstants.SUCCESS;

				} else {
					Result = "NewChPackageResult: " + NewChPackageResult + " , OldChPackageResult: " + OldChPackageResult;
				}
			}

		} catch (JSONException e) {

			return e.getMessage();
		}

		return Result;

	}*/
	
	/*private String disconnectDeviceFromSubscriber(String operatorUid, String hardwareUid) {

		Head response = beesmartAdminBean.disconnectDeviceFromSubscriber(operatorUid, hardwareUid);

		logger.info("Output From Subscriber : " + response.getDescription());

		if (response.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)) {
			
			logger.info("Device Successfully un-assigned to Subscriber. DeviceUid= " + hardwareUid);
			
			return PanacessConstants.SUCCESS;
			
		} else {
			
			logger.info("Device un-assigning Filed with DeviceUid= " + hardwareUid);
			
			return PanacessConstants.FAILURE + response.getDescription();
			
		}

	}
	   */
	
	
//  Device Changing Command Processing 
	/*public String deviceSwapCommandProcessing(PanacessProcessRequestData processRequestData) throws JSONException {

		
		 * {"clientId":5,"OldHWId":"5510010890","NewHWId":"5510010426",
		 * "services"
		 * :[{"serviceName":"AC","serviceIdentification":"NSC001","serviceType"
		 * :"TV"}]}
		 

		String output = null, deviceDisConnectResult = null, deviceConnectResult = null;

		JSONObject object = new JSONObject(processRequestData.getProduct());

		String oldHardwareId = object.getString("OldHWId");

		logger.info("Processing Device swap ... ");

		deviceDisConnectResult = disconnectDeviceFromSubscriber(processRequestData.getOfficeUId(), oldHardwareId);

		if (deviceDisConnectResult.equalsIgnoreCase(PanacessConstants.SUCCESS)) {

			deviceConnectResult = deviceCheckAndProcessing(processRequestData);

			if (deviceConnectResult.equalsIgnoreCase(PanacessConstants.SUCCESS)) {

				output = deviceConnectResult;

			} else {
				output = "Device DisConnection Result:" + deviceDisConnectResult + " , Device Assign Result:"
						+ deviceConnectResult;
			}

		} else {

			output = deviceDisConnectResult;
		}
		
		return output;
	}*/
	
	//release device
	/*public String releaseDeviceCommandProcessing(PanacessProcessRequestData processRequestData) {
		
		return disconnectDeviceFromSubscriber(processRequestData.getOfficeUId(), processRequestData.getHardwareId());
	}
	*/
	//Termination/SubscriberDelete Data Preparing and sending to Beenius
	/*public String TerminateCommandProcessing(PanacessProcessRequestData processRequestData) {
		
		Head response=beesmartAdminBean.deleteSubscriber(processRequestData.getOfficeUId(), processRequestData.getAccountNo());
		//delete id also
		logger.info("Output From Subscriber : "+response.getDescription());
		
		if(response.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)){
			
			return PanacessConstants.SUCCESS;
			
		} else {
			
			return PanacessConstants.FAILURE + response.getDescription();
			
		}
	}*/

	//OSD Message Sending
	/*public String beeniusOSDMessageCommandProcessing(PanacessProcessRequestData processRequestData) {
		
		String Message = processRequestData.getProduct();
		
		Head response=beesmartAdminBean.sendMessageToSubscriber(processRequestData.getAccountNo(), 
				PanacessConstants.OSD_MESSAGE_TYPE, processRequestData.getOfficeUId(), Message);
		//delete id also
			logger.info("Output From Subscriber : "+response.getDescription());
			
			if(response.getDescription().equalsIgnoreCase(PanacessConstants.RES_SUCCESSFUL)){
				
				return PanacessConstants.SUCCESS;
				
			} else {
				
				return PanacessConstants.FAILURE + response.getDescription();
				
			}
	}*/

}
