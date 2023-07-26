package courage.model.services.impl;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import courage.model.services.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.Message.RecipientType;

@Service
public class MailServiceImpl implements MailService {

	// @formatter:off
	private static final String HTML_REGEX = "<([A-Za-z][A-Za-z0-9]*)\\b[^>]*>(.*?)</\\1>";
	@Autowired private JavaMailSender mailSender;

	@Override // Send mail no attachment and text/plain
	public void sendSimpleMail(String subject, String text, RecipientType type, String...addresses) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setSubject(subject);
		sm.setText(text);
		if(type == RecipientType.CC) sm.setCc(addresses);
		else if(type == RecipientType.BCC) sm.setBcc(addresses);
		else sm.setTo(addresses);

		System.out.println("Sending by sendSimpleMail...");
		sm.setSentDate(new Date());
		mailSender.send(sm);
	}
	
	@Override // Send mail with attachment and text/html
	public void sendMimeMessage(String subject, String text, MultipartFile[] files, RecipientType type, String...addresses) 
		throws MessagingException {
		if(files == null && !text.matches(HTML_REGEX)) this.sendSimpleMail(subject, text, type, addresses);
		else {
			MimeMessageHelper mm = this.prepareMM(subject, text, type, addresses);
			if(files != null) for(MultipartFile file: files) mm.addAttachment(file.getOriginalFilename(), file);
			mailSender.send(mm.getMimeMessage());			
		}
	}

	@Override // Send mail with attachment and text/html
	public void sendMimeMessage(String subject, String text, File[] files, RecipientType type, String...addresses) 
		throws MessagingException {
		if(files == null && !text.matches(HTML_REGEX)) this.sendSimpleMail(subject, text, type, addresses);
		else {
			MimeMessageHelper mm = this.prepareMM(subject, text, type, addresses);
			if(files != null) for(File file: files) mm.addAttachment(file.getName(), file);
			mailSender.send(mm.getMimeMessage());			
		}
	}

	private MimeMessageHelper prepareMM(String subject, String text, RecipientType type, String...addresses) 
		throws MessagingException {
		MimeMessageHelper mm = new MimeMessageHelper(mailSender.createMimeMessage(), true, "UTF-8");
		mm.setSubject(subject);
		mm.setText(text, true);
		
		if(type == RecipientType.CC) mm.setCc(addresses);
		else if(type == RecipientType.BCC) mm.setBcc(addresses);
		else mm.setTo(addresses);

		mm.setSentDate(new Date());
		return mm;
	}
	
	// @formatter:on

}
