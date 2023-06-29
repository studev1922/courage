package courage.model.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import courage.model.services.FileUpload;

@Service
public class FileServiceImpl implements FileUpload {
	
	@Autowired private ServletContext context;
		
	@Override // get url on server - EX: http://localhost:8080/data/images/...
	public String pathServer(String...directories) {
		String uri = uri(directories);
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(uri).toUriString();
	}

	@Override // get local on this PC - EX: file://C:/.../src/main/data/images/...
	public String pathLocal(String...directories) {
		return context.getRealPath(uri(directories));
	}
	
	@Override // read all files're name
	public String[] fileNames(Boolean fileOrDir, String...directories) {
		File directory = new File(pathLocal(directories));

		return fileOrDir==null ? directory.list() // only file or folder else fileOrDir is null
			: directory.list((dir, name) -> fileOrDir==name.lastIndexOf(".")>-1);
	}
	
	public String saveFolder(String...directories) {
		return mkdirs(directories).getName();
	}
	
	@Override
	public String saveFile(MultipartFile file, String...directories) {
		return this.saveFile(null, file, directories);
	}
	
	@Override
	public String saveFile(String fileName, MultipartFile file, String...directories) {
		try {
			String directory = mkdirs(directories).getAbsolutePath();
			// check fileName
			boolean check = fileName == null;
			if(!check) check = fileName.isEmpty();
			
			Path path = Paths.get(directory, check ? file.getOriginalFilename() : fileName);
			if(!path.toFile().exists()) {
				file.transferTo(path);
				return check ? file.getOriginalFilename() : fileName;
			} else System.err.println("File name's "+fileName+" already exists, cannot be saved.");
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}	
		return null;
	}
	
	@Override // return all files're name saved
	public List<String> saveFile(MultipartFile[] files, String...directories) {
		if(files == null) return new ArrayList<>();
		String directory = mkdirs(directories).getAbsolutePath();
		
		List<String> list = new LinkedList<>();
		for(MultipartFile file : files) {
			try {
				String fileName = file.getOriginalFilename();
				Path path = Paths.get(directory, fileName);
				if(!path.toFile().exists()) {
					file.transferTo(path);
					list.add(fileName);
				} else System.err.println("File name's "+fileName+" already exists, cannot be saved.");
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		} return list;
	}
	
	@Override // delete file
	public void deleteFile(String uri) throws IOException {
		if(uri == null) return;
		File file = new File(context.getRealPath(uri(uri)));
		if(!file.getPath().matches(NOT_DELETE))
		if(!file.delete()) FileUtils.deleteDirectory(file);
	}
	
	@Override // delete all files're name
	public void deleteFiles(String[] fileNames, String...directories) {
		if(fileNames == null || directories == null) return;
		String dir = uri(directories);
		for(String fileName : fileNames) try {
			this.deleteFile(dir+"/"+fileName);
		} catch (Exception e) {System.err.println(e.getMessage());}
	}
	
	// return uri concat multiple directories to path - EX: /data/images/...
	public static final String uri(String...directories) {
		if(directories == null) return DEFAULT_FOLDER;
		else if(directories[0] == null) return DEFAULT_FOLDER;

		StringBuilder str =  new StringBuilder(
			(directories[0].startsWith(DEFAULT_FOLDER))
			? "" :DEFAULT_FOLDER
		);
		
		for(String directory : directories) str.append("/").append(directory);
		return str.toString();
	}

	// get file and make folders if not doesn't exist
	private File mkdirs(String...directories) {
		File file = new File(pathLocal(directories));
		if(!file.exists()) file.mkdirs();
		return file;
	}

}
