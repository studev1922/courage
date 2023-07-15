package courage.model.services;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * implements this code ༼ つ ◕_◕ ༽つ
 */
public interface FileUpload {

	static final String DEFAULT_FOLDER = "/uploads";
	static final String NOT_DELETE = "default.(\\w)*";

	/**
	 * hash code array String EX: ["a", 1, 3, x.jpg] => "...60b24251e1cef181.jpg"
	 * 
	 * @param values any
	 * @return name hashing as MD5 from values
	 */
	static String hashFileName(Object... values) {
		// @formatter:off | the last parameter must be the file type
		if(values==null || values.length == 0) 
			return FileUpload.digesToString(
				String.valueOf(System.currentTimeMillis()), "MD5"
			);
		
		StringBuffer str = new StringBuffer();
		String type = values[values.length - 1].toString();
		int dotAt = type.lastIndexOf("."); // .[type]
		type = dotAt>-1 ? type.substring(dotAt).trim() : new String();

		for (Object name : values) str.append(name); // append file
		return FileUpload.digesToString(str.toString(), "MD5")+type; // @formatter:on
	}

	/**
	 * @param input String to hash
	 * @return text hashing as MD5
	 */
	static String digesToString(String input, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);
			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32)
				hashtext = "0" + hashtext;
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return
	 */
	byte[] getFile(String... paths);

	/**
	 * @param directories are contain folders or files
	 * @return the path path to the server - EX:
	 *         http://localhost:8080/uploads/images/...
	 */
	String pathServer(String... directories);

	// get local on this PC - EX: file://C:/.../src/main/data/images/...
	String pathLocal(String... directories);

	/**
	 * @param fileOrDir   get only files or folders else get all by null
	 * @param directories are contain folders or files
	 * @return all file're path with condition's fileOrDir
	 *         <h3><b>EX: </b>[abc.png, bcd.jpg, cde.pdf, ...]</h3>
	 */
	String[] fileNames(Boolean fileOrDir, String... directories);

	/**
	 * @param directories to create
	 * @return directory created;
	 */
	String saveFolder(String... directories);

	/**
	 * @param file        is {@link MultipartFile} to save
	 * @param directories are contain folders or files
	 * @return file name has been saved
	 */
	String saveFile(MultipartFile file, String... directories);

	/**
	 * @param file        is {@link MultipartFile} to save
	 * @param fileName    to set name of file
	 * @param directories are contain folders or files
	 * @return file name has been saved
	 */
	String saveFile(String fileName, MultipartFile file, String... directories);

	/**
	 * @param files       are {@link MultipartFile} to save
	 * @param directories are contain folders or files
	 * @return all files're name has been saved
	 */
	List<String> saveFiles(MultipartFile[] files, String... directories);

	/**
	 * @param files       are {@link MultipartFile} to save
	 * @param isHashName  is hash new file's name
	 * @param directories are contain folders or files
	 * @return all files're name has been saved
	 */
	List<String> saveFiles(MultipartFile[] files, boolean isHashName, String... directories);

	/**
	 * @param uri to delete
	 * @throws IOException
	 */
	void deleteFile(String... uri) throws IOException;

	/**
	 * @param fileNames're name to delete in directories
	 * @param directories  are contain folders or files
	 */
	void deleteFiles(String[] fileNames, String... directories);
}
