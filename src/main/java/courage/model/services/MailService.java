package courage.model.services;

import java.io.File;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;
import jakarta.mail.Message.RecipientType;

/**
 * @see SendMail#sendSimpleMail(String, String, RecipientType, String...) no attachment
 * @see SendMail#sendMimeMessage(String, String, MultipartFile[], RecipientType, String...) with attachment
 */
public interface MailService {
	
	/**
	 * @param subject of mail
	 * @param text has type text/plain
	 * @param type {@link RecipientType} default TO or select CC or BCC
	 * @param addresses are emails're addresses to send
	 * 
	 * @see SendMail#sendSimpleMail(String, String, RecipientType, String...)
	 */
	public void sendSimpleMail(String subject, String text, RecipientType type, String...addresses);
	
	/**
	 * @param subject of mail
	 * @param text matches {@link SendMail#HTML_REGEX} call method 
	 * {@link SendMail#sendSimpleMail(String, String, RecipientType, String...)} or else this method
	 * @param files're attachments add to content
	 * @param type {@link RecipientType} default TO or select CC or BCC
	 * @param email are emails're addresses to send
	 * @throws MessagingException if multipart creation failed
	 * 
	 * @see SendMail#sendMimeMessage(String, String, File[], RecipientType, String...)
	 */
	public void sendMimeMessage(String subject, String text, MultipartFile[] files, RecipientType type, String...addresses) throws MessagingException;

	/**
	 * @param subject of mail
	 * @param text matches {@link SendMail#HTML_REGEX} call method 
	 * {@link SendMail#sendSimpleMail(String, String, RecipientType, String...)} or else this method
	 * @param files're attachments add to content
	 * @param type {@link RecipientType} default TO or select CC or BCC
	 * @param email are emails're addresses to send
	 * @throws MessagingException if multipart creation failed
	 * 
	 * @see SendMail#sendMimeMessage(String, String, File[], RecipientType, String...)
	 */
	public void sendMimeMessage(String subject, String text, File[] files, RecipientType type, String...addresses) throws MessagingException;
}

