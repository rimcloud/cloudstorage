package kr.co.crim.oss.rimdrive.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageSourceHelper {

    private static MessageSource messageSource = null;

    private MessageSourceHelper(MessageSource messageSource) {
	MessageSourceHelper.messageSource = messageSource;
    }

    public static MessageSource getMessageSource() {
	return messageSource;
    }

    public static String getMessage(String code) {
	String result = "";
	try {
	    result = messageSource == null ? code : messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
	} catch (Exception e) {
	    result = code;
	}

	return result;
    }

    public static String getMessage(String code, Object[] args) {
	return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

}
