import com.threlease.base.utils.Failable;
import org.springframework.web.multipart.MultipartFile;
import com.threlease.base.utils.Failable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileStore {
    private final String fileDir;

    public FileStore(String fileDir) {
        String rootPath = System.getProperty("user.dir");
        this.fileDir = rootPath + File.separator + fileDir;
    }

    public String getFullPath(String filename) { return fileDir + File.separator + filename; }

    public Failable<Boolean, String> deleteFile(String name) {
        Path path = Paths.get(this.fileDir + File.separator + name);

        try {
            Files.delete(path);
            return Failable.success(true);
        } catch (IOException e) {
            return Failable.error(e.getMessage());
        }
    }

    public boolean fileExist(String filename) {
        Path path = Paths.get(this.fileDir + File.separator + filename);

        return !Files.exists(path);
    }

    public Failable<UploadFile, String> storeFile(MultipartFile multipartFile) {

        if(multipartFile.isEmpty()) {
            return Failable.error("파일을 찾을 수 없습니다.");
        }

        String originalFilename = multipartFile.getOriginalFilename();

        String storeFilename = UUID.randomUUID() + "." + extractExt(originalFilename);

        if (!extractExt(originalFilename).equals("jpg") &&
                !extractExt(originalFilename).equals("png") &&
                !extractExt(originalFilename).equals("jpeg") &&
                !extractExt(originalFilename).equals("gif") &&
                !extractExt(originalFilename).equals("bmp")
        ) return Failable.error("이미지만 업로드해주세요.");

        try {
            multipartFile.transferTo(new File(getFullPath(storeFilename)));

            return Failable.success(new UploadFile(originalFilename, storeFilename));
        } catch (IOException e) {
            return Failable.error(e.getMessage());
        }
    }

    public Failable<String, String> readFile(String filename) {
        if (!fileExist(filename))
            return Failable.error("File Not Exist");

        try {
            return Failable.success(new String(Files.readAllBytes(Paths.get(this.fileDir + File.separator + filename))));
        } catch (IOException e) {
            return Failable.error("Error reading file: " + e.getMessage());
        }
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
