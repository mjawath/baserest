package com.techstart.base.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	String store(MultipartFile file);

	Stream<Path> loadAll();

	byte[]  load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

}


@Component
class StorageServiceImpl implements StorageService{

	private String filePath ="C:\\Users\\jawa\\Pictures\\shoes images - Google Search_files\\";

	private Logger log = LoggerFactory.getLogger(StorageServiceImpl.class);

	@Override
	public void init() {

	}

	@Override
	public String store(MultipartFile file) {
		String fileName = String.valueOf(UUID.randomUUID());

		String originalFilename = file.getOriginalFilename();
		String ext = originalFilename.split("\\.")[1];
		Path path = Paths.get(filePath+ fileName+"."+ext);
		try {
			Files.write(path, file.getBytes());
		} catch (IOException e) {
			log.error("error occured while uploading file",e);
		}
//		Paths.
		return path.getFileName().toString();
	}

	@Override
	public Stream<Path> loadAll() {
		return null;
	}

	@Override
	public byte[]  load(String filename) {


		try {

			FileInputStream fileInputStream = new FileInputStream(new File(filePath+filename));

			byte[] bytes = StreamUtils.copyToByteArray(fileInputStream);

			return bytes;
		}catch (Exception e){
			throw new RuntimeException("file error");
		}

	}

	@Override
	public Resource loadAsResource(String filename) {
		return null;
	}

	@Override
	public void deleteAll() {

	}
}