package com.example.sbuser.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;

public class Utility {

  public static String getTemplateString(ClassPathResource classPathResource) throws IOException {
    InputStream fileInputStream = classPathResource.getInputStream();
    int bufferSize = 1024;
    char[] buffer = new char[bufferSize];
    StringBuilder textContent = new StringBuilder();
    Reader in = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
    for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
      textContent.append(buffer, 0, numRead);
    }
    return textContent.toString();
  }
}
