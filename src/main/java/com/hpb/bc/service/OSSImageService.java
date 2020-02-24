package com.hpb.bc.service;

import org.springframework.web.multipart.MultipartFile;

public interface OSSImageService {
    String upload(MultipartFile file) throws Exception;
}
