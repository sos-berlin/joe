package sos.ftp.profiles;

import javax.crypto.Cipher;
import java.security.*;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/** @author re */
public class SOSCrypt {

    private static final String CHARSET = "UTF8";
    private static final String FILLCHARS = "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
            + "000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";

    public static String getKey(String pass, int length) throws Exception {
        if (pass == null || pass.isEmpty()) {
            throw new Exception("ivalid key length for encryption");
        }
        if (pass.length() < length) {
            pass += FILLCHARS.substring(0, length - pass.length());
        }
        if (pass.length() > length) {
            pass = pass.substring(0, length);
        }
        return pass;
    }

    public static String encrypt(String pass, String str) throws Exception {
        try {
            pass = getPadded(pass, 24);
            str = getPadded(str, 8);
            Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
            Security.addProvider(bp);
            Cipher encrypt = Cipher.getInstance("DESede/ECB/NoPadding", "BC");
            SecretKey key = new SecretKeySpec(pass.getBytes(), "DESede");
            encrypt.init(Cipher.ENCRYPT_MODE, key);
            byte[] utf8 = str.getBytes(SOSCrypt.CHARSET);
            byte[] enc = encrypt.doFinal(utf8);
            return new sun.misc.BASE64Encoder().encode(enc);
        } catch (Exception e) {
            throw new Exception("Could not encrypt: " + e.getMessage());
        }
    }

    public static String decrypt(String pass, String str) throws Exception {
        try {
            pass = getPadded(pass, 24);
            Provider bp = new org.bouncycastle.jce.provider.BouncyCastleProvider();
            Security.addProvider(bp);
            Cipher decrypt = Cipher.getInstance("DESede/ECB/NoPadding", "BC");
            SecretKey key = new SecretKeySpec(pass.getBytes(), "DESede");
            decrypt.init(Cipher.DECRYPT_MODE, key);
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            byte[] utf8 = decrypt.doFinal(dec);
            return new String(utf8, SOSCrypt.CHARSET).trim();
        } catch (Exception e) {
            throw new Exception("Could not decrypt: " + e.getMessage());
        }
    }

    public static String getPadded(String in, int size) {
        int slen = in.length() % size;
        int i = size - slen;
        if (i > 0 && i < size) {
            StringBuffer buf = new StringBuffer(in.length() + i);
            buf.insert(0, in);
            for (i = size - slen; i > 0; i--) {
                buf.append(" ");
            }
            return buf.toString();
        } else {
            return in;
        }
    }

    public static void main(String args[]) throws Exception {
        String text = "abc";
        String sencrypt = encrypt("12345678", text);
        System.out.println(text + " -> encrypt in -> " + sencrypt);
        String sdecrypt = decrypt("12345678", sencrypt);
        System.out.println(sencrypt + " -> decrypt in -> " + sdecrypt);
    }

}