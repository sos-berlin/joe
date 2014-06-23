package sos.scheduler.editor.app;
/*CREATE OR REPLACE AND COMPILE JAVA SOURCE NAMED
    SOSCrypt
  AS
*/
import javax.crypto.Cipher;
import java.security.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sos.joe.globals.messages.ErrorLog;
import com.sos.joe.globals.messages.Messages;
import com.sos.joe.globals.options.Options;


/**
 * @author re
 * 
 */
public class SOSCrypt {

    // usage:
    // select sos_encrypt('key', 'data') from dual;

    /** Verwendete Zeichendecodierung */
    private static final String charset = "UTF8";

    /** Verwendete Füllzeichen */
    private static final String fillchars = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";


    /**
     * Schlüssel auffüllen
     * 
     * @param pass   Schlüssel
     * @param length gewünschte Länge
     * @return
     * @throws Exception
     */
    public static String getKey(String pass, int length) throws Exception {

        if (pass == null || pass.length() == 0) {
            throw new Exception("ivalid key length for encryption");
        }

        if (pass.length() < length) {
          pass += fillchars.substring(0,length-pass.length());
        }

        if (pass.length() > length) {
          pass = pass.substring(0,length);
        }

        return pass;
    }
   
    
    /**
     * Verschlüsseln
     * 
     * @param pass   Schlüssel
     * @param str    Daten
     * @return
     * @throws Exception
     */
    public static String encrypt(String pass, String str) throws Exception {
        
        /*
        if(pass == null || pass.getBytes().length != 8){
            throw new Exception("ivalid key length for encryption");
        }
        */
        
        try {
	
            // Auffüllen des Schlüssel auf die vom Algorithmus benötigte Länge
            pass = getPadded(pass, 24);
        	//pass = getPadded(pass, 8);

            // Auffüllen der Daten auf die vom Algorithmus benötigte Länge
            str = getPadded(str, 8);

            // nur für jre 1.3.x nötig
            Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();

            Security.addProvider(bp);

            //Cipher encrypt = Cipher.getInstance("DES");            
            //SecretKey key = new SecretKeySpec(pass.getBytes(), "DES");
            
            Cipher encrypt = Cipher.getInstance("DESede/ECB/NoPadding", "BC");

            SecretKey key = new SecretKeySpec(pass.getBytes(), "DESede");

            encrypt.init(Cipher.ENCRYPT_MODE, key);

            // encode string mit utf8            
            byte[] utf8 = str.getBytes(SOSCrypt.charset);

            // encrypt
            byte[] enc = encrypt.doFinal(utf8);

            // encode bytes zu base64
            return new sun.misc.BASE64Encoder().encode(enc);

        } catch (Exception e) {
        	new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; ..could not encrypt.", e);
            throw new Exception("Could not encrypt: " + e.getMessage());
        }
    }

    /**
     * Entschlüsseln
     * 
     * @param pass Schlüssel
     * @param str  Daten
     * @return
     * @throws Exception
     */
    public static String decrypt(String pass, String str) throws Exception {

        /*
        if(pass == null || pass.getBytes().length != 8){
            throw new Exception("ivalid key length for decrypt");
        }
        */
        
        try {

            // Auffüllen des Schlüssel auf die vom Algorithmus benötigte Länge
            pass = getPadded(pass, 24);
        	//pass = getPadded(pass, 8);

            // Auffüllen der Daten auf die vom Algorithmus benötigte Länge
            //str = getPadded(str, 8);

            // nur für jre 1.3.x nötig
            Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();

            Security.addProvider(bp);


            //Cipher decrypt = Cipher.getInstance("DES");
            //SecretKey key = new SecretKeySpec(pass.getBytes(), "DES");
            Cipher decrypt = Cipher.getInstance("DESede/ECB/NoPadding", "BC");

            SecretKey key = new SecretKeySpec(pass.getBytes(), "DESede");

            decrypt.init(Cipher.DECRYPT_MODE, key);


            // decode base64 zu bytes                		            
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

            // decrypt
            byte[] utf8 = decrypt.doFinal(dec);//->javax.crypto.IllegalBlockSizeException: data not block size aligned

            // decode mit utf-8
            return new String(utf8, SOSCrypt.charset).trim();

        } catch (Exception e) {        	
        	new ErrorLog("error in " + sos.util.SOSClassUtil.getMethodName() + " ; ..could not decrypt.", e);
            throw new Exception("Could not decrypt: " + e.getMessage());
        }

    }

    
    public static String getPadded(String in, int size) {
    	
    	int slen = (in.length() % size);
    	int i = (size - slen);
    	if ((i > 0) && (i < size)){
    		StringBuffer buf = new StringBuffer(in.length() + i);
    		buf.insert(0, in);
    		for (i = (size - slen); i > 0; i--) {
    			buf.append(" ");
    		}
    		return buf.toString();
   		}
   		else {
   			return in;
   		}
   	}
    
    public static void main( String args[] ) throws Exception
	  {
		String text = "abc";
		String sencrypt = encrypt("12345678", text);
		System.out.println( text + " -> encrypt in -> " + sencrypt);
		
		String sdecrypt = decrypt("12345678", sencrypt);
		System.out.println(sencrypt + " -> decrypt in -> " + sdecrypt);
		
	  }
}
//;