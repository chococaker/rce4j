package org.rce4j.net;

import java.io.IOException;
import java.io.InputStream;

/**
 * A class representing a file uploaded via an HTTP request.
 */
public interface UploadedFile {
    /**
     * @return The name of the file that was sent via the request.
     */
    String getName();
    
    /**
     * @return An {@link InputStream} containing the file's contents.
     */
    InputStream getInputStream() throws IOException;
}
