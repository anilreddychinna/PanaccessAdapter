package com.obs.integrator.panacess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Queue;

import javax.management.RuntimeErrorException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.security.cert.X509Certificate;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class PanacessProducer implements Runnable {

	private int no,recordsCount;
	private String encodedPassword,provisioningSystem;
	PropertiesConfiguration prop;
	BufferedReader br = null;
	private Queue<PanacessProcessRequestData> messageQueue;
	private static HttpGet getRequest;
	private static byte[] encoded;
	private static String tenantIdentifier;
	private static HttpResponse response;
	private static HttpClient httpClient;
	private static Gson gsonConverter = new Gson();
	private int wait;
	static Logger logger = Logger.getLogger("");
	private String command;

	public static HttpClient wrapClient(HttpClient base) {

		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				@SuppressWarnings("unused")
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				@SuppressWarnings("unused")
				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
					// TODO Auto-generated method stub

				}

				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
					// TODO Auto-generated method stub

				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = base.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			return new DefaultHttpClient(ccm, base.getParams());
		} catch (Exception ex) {
			return null;
		}
	}

	public PanacessProducer(Queue<PanacessProcessRequestData> messageQueue1,
			PropertiesConfiguration prop1) {
		// 1. Here intialize connection object for connecting to the RESTful
		// service
		// 2. Connect to RESTful service
		this.messageQueue = messageQueue1;
		this.prop = prop1;
		httpClient = new DefaultHttpClient();
		httpClient = wrapClient(httpClient);
		String username = prop.getString("username");
		String password = prop.getString("password");
		provisioningSystem = prop.getString("provisioningSystem");
	    recordsCount = prop.getInt("recordsCount");
		wait = prop.getInt("ThreadSleep_period");
		encodedPassword = username.trim() + ":" + password.trim();
		tenantIdentifier = prop.getString("tenantIdentfier");	
		command = prop.getString("BSSServerQuery").trim()+"/beenius?no="+recordsCount+"&provisioningSystem="+provisioningSystem;
		getRequest = new HttpGet(command);
		//getRequest = new HttpGet(prop.getString("BSSServerQuery").trim()+"?no="+recordsCount+"&provisioningSystem="+provisioningSystem);
		encoded = Base64.encodeBase64(encodedPassword.getBytes());
		getRequest.setHeader("Authorization", "Basic " + new String(encoded));
		getRequest.setHeader("Content-Type", "application/json");
		getRequest.addHeader("X-Mifos-Platform-TenantId", tenantIdentifier);
		getRequest.addHeader("X-Obs-Platform-TenantId", tenantIdentifier);
		readDataFromRestfulService();
	}

	@Override
	public void run() {
		while (true) {
			produce();	   
			try {
				Thread.sleep(wait);
			} catch (InterruptedException ex) {
				logger.error("thread is Interrupted for the : " + ex.getCause().getLocalizedMessage());
			}
		}
	}

	/**
	 * Make a RESTful call to fetch the list of messages and add to the message
	 * queue for processing by the consumer thread.
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private void produce() {
		try {
			logger.info("Produce() class calling ...");
		    synchronized (messageQueue) {
			   if (messageQueue.isEmpty()) {
				readDataFromRestfulService();
				messageQueue.notifyAll();
			   
				}else{
					logger.info(" records are Processing .... ");
					messageQueue.notifyAll();
					Thread.sleep(wait);
				}
			 } 

		} catch (InterruptedException e) {
			logger.error("thread is Interrupted for the : " + e.getCause().getLocalizedMessage());
		}

	}

	/**
	 * Change the Message.java class accordingly as per the JSON string of the
	 * respective RESTful API Read the JSON data from the RESTful API.
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 * 
	 */
	private void readDataFromRestfulService() {

		
		try {
            no=1;
			response = httpClient.execute(getRequest);
			if (response.getStatusLine().getStatusCode() == 401) {
				logger.error("Authentication Failed : HTTP error code is: "
						+ response.getStatusLine().getStatusCode());
				httpClient.getConnectionManager().shutdown();	
				throw new AuthenticationException("AuthenticationException :  BSS system server username (or) password you entered is incorrect . check in the PacketspanIntegrator.ini file");		
			}
			else if(response.getStatusLine().getStatusCode() == 404){
				logger.error("Resource Not Found Exception : HTTP error code is: " + response.getStatusLine().getStatusCode());
				httpClient.getConnectionManager().shutdown();
				throw new RuntimeErrorException(null, "Resource NotFound Exception :  BSS server system 'BSSServerQuery' url error.");			
			}
			else if(response.getStatusLine().getStatusCode() != 200){
				logger.error("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
				String errorOutput,error = "";
				BufferedReader br1 = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
				while((errorOutput = br1.readLine()) != null){
					error = errorOutput;
				}
				br1.close();
				throw new Exception(error);			
			}
			
			br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			String output;
			while ((output = br.readLine()) != null) {
			
				Type collectionType = new TypeToken<List<EntitlementsData>>() {
				}.getType();
				@SuppressWarnings("unchecked")
				List<EntitlementsData> entitlementData = (List<EntitlementsData>) gsonConverter.fromJson(output, collectionType);
				int length = entitlementData.size();
				if (length > 0) {
					for (EntitlementsData entitlement : entitlementData) {
						setDataForEntitlement(entitlement);		
					}
				}
			}
			br.close();
		  
		} catch (ClientProtocolException e) {
			String errorMessage = "ClientProtocolException : " + e.getCause() + ". Please Verify the HttpClient.execute() Method";
			PanacessThreadedQueueAdapter.sendToUserEmail(errorMessage);
			logger.error("ClientProtocolException : " + e.getCause().getLocalizedMessage());
		
		} catch (IOException e) {
			String errorMessage = "IOException : " + e.getCause() + ". verify the BSS system server running or not";
			PanacessThreadedQueueAdapter.sendToUserEmail(errorMessage);
			logger.error(errorMessage);		
				try {
					Thread.sleep(wait);
				} catch (InterruptedException e1) {
					logger.error("thread is Interrupted for the : " + e1.getCause().getLocalizedMessage());
				}
		
		} catch (AuthenticationException e) {						
			
			String errorMessage = "AuthenticationException : " + e.getCause() + ". verify the BSS system Username and Password.";
			PanacessThreadedQueueAdapter.sendToUserEmail(errorMessage);
			logger.info("##################################################################################");
			logger.info("");
			logger.error("AuthenticationException: " + e.getLocalizedMessage());
			logger.info("");
			logger.info("##################################################################################");
			logger.info("");
			logger.info("Stoping Adapter...");
			System.exit(0);
			
		} catch (RuntimeErrorException e) {
			
			String errorMessage = "RuntimeErrorException : " + e.getCause();
			PanacessThreadedQueueAdapter.sendToUserEmail(errorMessage);
			logger.info("##################################################################################");
			logger.info("");
			logger.error("RuntimeErrorException: " + e.getLocalizedMessage());
			logger.info("");
			logger.info("##################################################################################");
			logger.info("");
			logger.info("Stoping Adapter...");
			System.exit(0);
			
		} catch (Exception e) {	
			String errorMessage = "Exception : " + e.getCause();
			PanacessThreadedQueueAdapter.sendToUserEmail(errorMessage);
			logger.error("Exception: " + e.getLocalizedMessage());			
		}

	}

	private void setDataForEntitlement(EntitlementsData entitlement) {
		// TODO Auto-generated method stub
		PanacessProcessRequestData m;
		try {
			
			if (entitlement.getProvisioingSystem().equalsIgnoreCase(provisioningSystem))
			 {					
				m = new PanacessProcessRequestData(entitlement.getId(),entitlement.getPrdetailsId(),entitlement.getProvisioingSystem(),
						entitlement.getServiceId(),entitlement.getProduct(),entitlement.getHardwareId(),entitlement.getRequestType(),
						entitlement.getItemCode(),entitlement.getItemDescription(),entitlement.getClientId(),entitlement.getAccountNo(),
						entitlement.getFirstName(),entitlement.getLastName(),entitlement.getOfficeUId(),entitlement.getBranch(),
						entitlement.getRegionCode(),entitlement.getRegionName(),entitlement.getDeviceId(),entitlement.getIpAddress(), entitlement.getDisplayName());
				
				
				logger.info(no +") ClientId="+entitlement.getClientId()+" , ClientName = "+entitlement.getFirstName() +" , id= "+ entitlement.getId()+" , ServiceId = "+ entitlement.getServiceId() +" , product/Message = "+"'"+ entitlement.getProduct()+"'"+" , setupboxid/SerialNo/HardWareId ="
						+entitlement.getHardwareId()+" , requestType = "+entitlement.getRequestType()+" , itemCode="+entitlement.getItemCode()+
						" , RegionCode="+entitlement.getRegionCode()+" , operatorUID="+entitlement.getOfficeUId()+", displayName="+entitlement.getDisplayName());
				messageQueue.offer(m);
				no = no + 1;
				
			}
		} catch(Exception e){
			logger.error("Exception : " + e.getCause() + " .");
		}

	}
}
