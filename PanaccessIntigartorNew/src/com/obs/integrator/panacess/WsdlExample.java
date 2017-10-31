
package com.obs.integrator.panacess;

import java.net.MalformedURLException;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import de.panaccess.cableview.wsdl.v4_0.operator.ProductEntryList;



public class WsdlExample {

	public static void main(String[] args) throws MalformedURLException, ServiceException, RemoteException {
		String endpoint = "https://cv01.panaccess.com/index.php?requestMode=wsdl&v=3.0&r=operator&wsdl";
		String userName = "demo";
		String password = "demo2010";

	//	CableViewService service = new CableViewServiceLocator();
		//CableViewServicePortType cvService = service.getCableViewServicePort(new URL(endpoint));

		//Logging into CableView
	//	String sessionId = cvService.cvLogin(userName, password);


		int offset = 0;
		int limit = 100;
		ProductEntryList list;
		/*do {
	//		list =  cvService.cvGetListOfProducts(sessionId, offset, limit, "name", "asc", null);
			for (ProductEntry entry : list.getProductEntries()) {
				System.out.println(entry.getProductId()+": "+entry.getName());
			}
			offset += limit;
		}
		while (list.getCount() > offset);
		
		cvService.cvLogout(sessionId);*/

	}
}
