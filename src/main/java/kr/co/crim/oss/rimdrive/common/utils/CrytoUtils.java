package kr.co.crim.oss.rimdrive.common.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrytoUtils {

    private static final Logger logger = LoggerFactory.getLogger(CrytoUtils.class);
    public static final String AES = "AES";
    private static final int KEY_LENGTH = 16;

    public static Key getAESKey() throws Exception {

	Key keySpec;

	String configKey = PropertyConfigurerHelper.getTripleDESkey();
	int configKeyLength = configKey.length();

	String key = configKey;

	if (configKeyLength > KEY_LENGTH) {
	    key = StringUtils.substring(key, KEY_LENGTH);
	} else if (configKeyLength < KEY_LENGTH) {
	    String tempStr = "A";
	    for (int i = configKeyLength; i < KEY_LENGTH; i++) {
		key += tempStr;
	    }
	}

	byte[] keyBytes = key.getBytes("UTF-8");

	keySpec = new SecretKeySpec(keyBytes, AES);

	return keySpec;
    }

    public static String _encrypt(String str) {
	String returnValue = "";
	try {
	    Key keySpec = getAESKey();

	    Cipher c = Cipher.getInstance(AES);
	    c.init(Cipher.ENCRYPT_MODE, keySpec);
	    byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
	    String enStr = new String(Base64.encodeBase64(encrypted));

	    returnValue = enStr;
	} catch (Exception e) {
	    logger.error("Exception : {}", e);
	    returnValue = "";
	}

	return returnValue;
    }

    public static String _decrypt(String enStr) {
	String returnValue = "";
	try {
	    Key keySpec = getAESKey();

	    Cipher c = Cipher.getInstance(AES);
	    c.init(Cipher.DECRYPT_MODE, keySpec);

	    byte[] byteStr = Base64.decodeBase64(enStr.getBytes("UTF-8"));
	    String decStr = new String(c.doFinal(byteStr), "UTF-8");
	    returnValue = decStr;
	} catch (Exception e) {
	    logger.error("Exception : {}", e);
	    returnValue = "";
	}
	return returnValue;
    }

}
