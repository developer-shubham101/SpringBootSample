package in.newdevpoint.bootcamp.utility;

import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUtility {

  private static final Logger logger = LoggerFactory.getLogger(FileUtility.class);

  public static String fileUpload(MultipartFile file, String absoluteFileUrl) {
    InputStream inputStream;
    OutputStream outputStream;

    String fileName = file.getOriginalFilename();
    File newFile = new File(absoluteFileUrl + fileName);

    try {
      inputStream = file.getInputStream();

      if (!newFile.exists()) {
        boolean isFiledSaved = newFile.createNewFile();
        logger.info("isFiledSaved: " + isFiledSaved);
      }
      outputStream = new FileOutputStream(newFile);
      int read;
      byte[] bytes = new byte[1024];

      while ((read = inputStream.read(bytes)) != -1) {
        outputStream.write(bytes, 0, read);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return newFile.getAbsolutePath();
  }
}
