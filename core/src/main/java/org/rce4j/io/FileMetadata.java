package org.rce4j.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

@SuppressWarnings("unused")
public class FileMetadata {
    public FileMetadata(File file) throws IOException {
        BasicFileAttributes attr = Files.readAttributes(Path.of(file.getPath()), BasicFileAttributes.class);
        
        creationTime = attr.creationTime().toString();
        lastAccessTime = attr.lastAccessTime().toString();
        lastModifiedTime = attr.lastModifiedTime().toString();
        
        size = attr.size();
        isHidden = file.isHidden();
    }
    
    private final String creationTime;
    private final String lastAccessTime;
    private final String lastModifiedTime;
    
    private final long size;
    private final boolean isHidden;
    
    public String getCreationTime() {
        return creationTime;
    }
    
    public String getLastAccessTime() {
        return lastAccessTime;
    }
    
    public String getLastModifiedTime() {
        return lastModifiedTime;
    }
    
    public long getSize() {
        return size;
    }
    
    public boolean isHidden() {
        return isHidden;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMetadata that = (FileMetadata) o;
        return size == that.size && isHidden == that.isHidden && Objects.equals(creationTime, that.creationTime) && Objects.equals(lastAccessTime, that.lastAccessTime) && Objects.equals(lastModifiedTime, that.lastModifiedTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(creationTime, lastAccessTime, lastModifiedTime, size, isHidden);
    }
    
    @Override
    public String toString() {
        return "FileMetadata{" +
                "creationTime=" + creationTime +
                ", lastAccessTime=" + lastAccessTime +
                ", lastModifiedTime=" + lastModifiedTime +
                ", size=" + size +
                ", isHidden=" + isHidden +
                '}';
    }
}
