package courage.model.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


/**
 * implements this code ༼ つ ◕_◕ ༽つ
 */
public interface FileUpload {
	
	static final String DEFAULT_FOLDER = "/uploads";
	static final String NOT_DELETE = "default.(\\w)*";;

	/**
	 * @return
	 */
	byte[] getFile(String...paths);
	
	/**
	 * @param directories are contain folders or files
	 * @return the path path to the server - EX: http://localhost:8080/uploads/images/...
	 */
	String pathServer(String...directories);

	// get local on this PC - EX: file://C:/.../src/main/data/images/...
	String pathLocal(String...directories);

	/**
	 * @param fileOrDir get only files or folders else
	 * @param directories are contain folders or files
	 * @return all file're path with condition's fileOrDir
	 * <h3><b>EX: </b>[abc.png, bcd.jpg, cde.pdf, ...]</h3>
	 */
	String[] fileNames(Boolean fileOrDir, String...directories);

	/**
	 * @param directories to create
	 * @return directory created;
	 */
	String saveFolder(String...directories);
	
	/**
	 * @param file is {@link MultipartFile} to save
	 * @param directories are contain folders or files
	 * @return file name has been saved
	 */
	String saveFile(MultipartFile file, String...directories);
	
	/**
	 * @param file is {@link MultipartFile} to save
	 * @param fileName to set name of file
	 * @param directories are contain folders or files
	 * @return file name has been saved
	 */
	String saveFile(String fileName, MultipartFile file, String...directories);
	
	/**
	 * @param files are {@link MultipartFile} to save
	 * @param directories are contain folders or files
	 * @return all files're name has been saved
	 */
	List<String> saveFile(MultipartFile[] files, String...directories);
	
	/**
	 * @param uri to delete
	 * @throws IOException 
	 */
	void deleteFile(String uri) throws IOException;

	/**
	 * @param fileNames're name to delete in directories
	 * @param directories are contain folders or files
	 */
	void deleteFiles(String[] fileNames, String...directories);
}
