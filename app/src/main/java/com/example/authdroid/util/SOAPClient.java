package com.example.authdroid.util;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * This is custom made ksoap2-android client which calls two different web services.
 * This is low level workaround because Android API doesn't support SOAP Web services
 * at this time, neither does google promise support in future. 
 * Client is capable of uploading image as base64 encoded string to server,
 * and to call authentication methods. 
 * @author emir
 *
 */
public class SOAPClient {

	private String uploadServiceURL;
	private String authServiceURL;
	
	private String NAMESPACE = "http://service.pca.burch.org/";
	private String UPLOAD_METHOD_NAME = "uploadFile";
	private String AUTH_METHOD_NAME = "authenticate";
		
	public SOAPClient(String baseServiceURL){
		this.uploadServiceURL = baseServiceURL.concat("/upload");
		this.authServiceURL = baseServiceURL.concat("/authenticate");
	}
	
	public void upload(String imgBase64, String fileName, String fileType){
		
		SoapObject request = new SoapObject(NAMESPACE, UPLOAD_METHOD_NAME);
		SoapObject uploadEntity = new SoapObject("", "uploadEntity");
		
		PropertyInfo pi = new PropertyInfo();
        pi.setName("FileName");//XML Element as defined in soap request for uploadFile method
        pi.setValue(fileName);
        pi.setType(String.class);
        uploadEntity.addProperty(pi);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("FileType");//XML Element as defined in soap request for uploadFile method
        pi2.setValue(fileType);
        pi2.setType(String.class);
        uploadEntity.addProperty(pi2);
        
        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("DataPayload");//XML Element as defined in soap request for uploadFile method
        pi3.setValue(imgBase64);
        pi3.setType(String.class);
        uploadEntity.addProperty(pi3);
                
        request.addProperty("uploadEntity", uploadEntity);
		
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        
        HttpTransportSE transport = new HttpTransportSE(uploadServiceURL, 600000);
        transport.debug = true;
        
        try
        {
        	transport.call("", envelope);
        }
        catch(XmlPullParserException e)
        {
            /*In most cases this exception will be caught, even when server returns correct response, and
             * web service call is sucessfull.
             */
        	Log.d("soap-rq-dmp(upload)", transport.requestDump);
            Log.d("soap-rsp-dmp(upload)", transport.responseDump);
        } catch (IOException e) {
			Log.e("soap-rq(upload)", e.getMessage());
		}
    }
	
	public Boolean authenticate(String euclidTreshold, String imageName){
		/*Fill the soap authentication request with required parameters*/
		SoapObject request = new SoapObject(NAMESPACE, AUTH_METHOD_NAME);
		SoapObject authDescriptor = new SoapObject("", "descriptor");
		
		PropertyInfo pi = new PropertyInfo();
        pi.setName("euclidTreshold");
        pi.setValue(euclidTreshold);
        pi.setType(String.class);
        authDescriptor.addProperty(pi);

        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("imageFileName");
        pi2.setValue(imageName);
        pi2.setType(String.class);
        authDescriptor.addProperty(pi2);
        
        request.addProperty("descriptor", authDescriptor);
        
        /*Create SOAP envelope to be send to service*/
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        
        HttpTransportSE transport = new HttpTransportSE(authServiceURL);
        transport.debug = true;
        
        try
        {
        	/*Call authentication service, SOAP ACTION is empty in WSDL, so leave it empty here too*/
        	transport.call("", envelope);
        }
        catch(XmlPullParserException e){ 
        	Log.e("soap-rq(auth)", e.getMessage());
        }catch (IOException e) {
			Log.e("soap-rq(auth)", e.getMessage());
		}
        Log.d("soap-rq-dmp(auth)", transport.requestDump);
        Log.d("soap-rsp-dmp(auth)", transport.responseDump);
        
        SoapObject response = null;
		try{
			response = (SoapObject)envelope.bodyIn;
		}catch (Exception e){
			Log.e("soap-response-parsing", e.getMessage());
		}
		
		if(response != null ){
			Boolean result =  Boolean.parseBoolean(response.getProperty(0).toString());
			if(result != null){
				return result;
			}else{
				return Boolean.FALSE;
			}
		}
		return null;
	}
}
