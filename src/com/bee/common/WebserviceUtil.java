package com.bee.common;

import android.widget.Toast;

import com.bee.app.ui.CItem;
import com.bee.bvo.WsBvo;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebserviceUtil {
    public static ArrayList<CItem> getIno(String strArg,String url) throws IOException, XmlPullParserException {
        System.out.println("rpc------");
        SoapObject rpc = new SoapObject("http://tempuri.org/", "GetZL");
        System.out.println("rpc" + rpc);
        System.out.println("strArg is " + strArg);
        rpc.addProperty("strArg", strArg); 
        /*rpc.addProperty("strSJH", strArg);*/

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = rpc;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE ht = new HttpTransportSE(url);

        ht.debug = true;

        ht.call("http://tempuri.org/GetZL", envelope);

        SoapObject detail =(SoapObject) envelope.getResponse();

        return parseSoapObject(detail);

    }
    

    private static ArrayList<CItem> parseSoapObject(SoapObject soapobject){
        ArrayList<CItem> lst = new ArrayList<CItem>();
        int totalCount = soapobject.getPropertyCount();
        for (int detailCount = 0; detailCount < totalCount; detailCount++) {
            SoapObject pojoSoap = (SoapObject) soapobject.getProperty(detailCount);
            CItem item = new CItem(pojoSoap.getProperty("DICTNO").toString(), pojoSoap.getProperty("DICTNAME").toString());
            lst.add(item);
        }
        return lst;
    }
    
    public static ArrayList<CItem> getORG(String strArg,String strSJH,String url) throws IOException, XmlPullParserException {
        SoapObject rpc = new SoapObject("http://tempuri.org/", "GetORG");

        rpc.addProperty("strArg", strArg); 
        rpc.addProperty("strSJH", strSJH);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = rpc;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE ht = new HttpTransportSE(url);

        ht.debug = true;

        ht.call("http://tempuri.org/GetORG", envelope);

        SoapObject detail =(SoapObject) envelope.getResponse();

        return parseSoapObjectORG(detail);

    }
    
    private static ArrayList<CItem> parseSoapObjectORG(SoapObject soapobject){
        ArrayList<CItem> lst = new ArrayList<CItem>();
        int totalCount = soapobject.getPropertyCount();
        for (int detailCount = 0; detailCount < totalCount; detailCount++) {
            SoapObject pojoSoap = (SoapObject) soapobject.getProperty(detailCount);
            CItem item = new CItem(pojoSoap.getProperty("ORGID").toString(), pojoSoap.getProperty("ORGNAME").toString());
            lst.add(item);
        }
        return lst;
    }
    
    

}
