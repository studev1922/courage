package courage.model.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletContext;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import courage.model.services.UploadService;

@Service
public class UploadServiceImpl implements UploadService {

	@Autowired
	private ServletContext context;

	@Override // get url on server - EX: http://localhost:8080/data/images/...
	public String pathServer(String... directories) {
		String uri = uri(directories);
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(uri).toUriString();
	}

	@Override // get local on this PC - EX: file://C:/.../src/main/data/images/...
	public String pathLocal(String... directories) {
		return context.getRealPath(uri(directories));
	}

	@Override // read file as bytes
	public byte[] getFile(String... paths) {
		try {
			return Files.readAllBytes(Paths.get(pathLocal(paths)));
		} catch (Exception e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	@Override // read all files're name
	public String[] fileNames(Boolean fileOrDir, String... directories) {
		File file = new File(pathLocal(directories));

		return fileOrDir == null ? file.list() // only file or folder else fileOrDir is null
				: file.list((dir, name) -> fileOrDir == name.lastIndexOf(".") > -1);
	}

	public String saveFolder(String... directories) {
		return mkdirs(directories).getName();
	}

	@Override
	public String saveFile(MultipartFile file, String... directories) {
		return this.saveFile(null, file, directories);
	}

	@Override
	public String saveFile(String fileName, MultipartFile file, String... directories) {
		try {
			String directory = mkdirs(directories).getAbsolutePath();
			boolean check = fileName == null || fileName.isEmpty();

			Path path = Paths.get(directory, check ? file.getOriginalFilename() : fileName);
			if (!path.toFile().exists()) {
				file.transferTo(path);
				return check ? file.getOriginalFilename() : fileName;
			} else
				System.err.println("File name's " + fileName + " already exists, cannot be saved.");
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> saveFiles(MultipartFile[] files, String... directories) {
		return this.saveFiles(files, false, directories); // save with base name
	}

	@Override // return all files're name saved
	public List<String> saveFiles(MultipartFile[] files, boolean isHashName, String... directories) {
		if (files == null)
			return new ArrayList<>();
		List<String> list = new ArrayList<>(files.length);
		String directory = mkdirs(directories).getAbsolutePath();
		String fileName;

		if (isHashName)
			for (MultipartFile file : files) {
				fileName = UploadService.hashFileName(
						System.currentTimeMillis(),
						file.getOriginalFilename());
				this.transferTo(directory, fileName, file, list);
			}
		else
			for (MultipartFile file : files) {
				fileName = file.getOriginalFilename();
				this.transferTo(directory, fileName, file, list);
			}
		return list;
	}

	@Override // doesn't delete file matches NOT_DELETE && start with http...
	public void deleteFile(String... uries) throws IOException {
		if (uries == null || uries.length == 0)
			return; // empty source
		else if (uries[uries.length - 1].startsWith("http"))
			return; // not delete  embeded from other source

		File file = new File(context.getRealPath(uri(uries)));
		String path = file.getPath();
		if (!path.matches(NOT_DELETE)) // delete and again if failed
			if (!file.delete())
				FileUtils.deleteDirectory(file);
		// delete file || delete directory
	}

	@Override // delete all files're name
	public void deleteFiles(String[] fileNames, String... directories) {
		if (fileNames == null || directories == null)
			return;
		String dir = uri(directories);
		for (String fileName : fileNames)
			try {
				this.deleteFile(dir + "/" + fileName);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
	}

	// return uri concat multiple directories to path - EX: /data/images/...
	static final String uri(String... directories) {
		if (directories == null)
			return DEFAULT_FOLDER;
		else if (directories[0] == null)
			return DEFAULT_FOLDER;

		StringBuilder str = new StringBuilder(
				(directories[0].startsWith(DEFAULT_FOLDER))
						? ""
						: DEFAULT_FOLDER);

		for (String directory : directories)
			str.append("/").append(directory);
		return str.toString();
	}

	// get file and make folders if not doesn't exist
	private File mkdirs(String... directories) {
		File file = new File(pathLocal(directories));
		if (!file.exists())
			file.mkdirs();
		return file;
	}

	private void transferTo(String directory, String fileName, MultipartFile file, List<String> list) {
		try {
			Path path = Paths.get(directory, fileName);
			if (!path.toFile().exists()) {
				file.transferTo(path);
				list.add(fileName);
			} else
				System.err.println("File name's " + fileName + " already exists, cannot be saved.");
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
	}

}
