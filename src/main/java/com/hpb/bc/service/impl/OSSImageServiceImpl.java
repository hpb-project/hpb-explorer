package com.hpb.bc.service.impl;

import com.hpb.bc.service.OSSImageService;
import com.hpb.bc.util.OSSClientUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OSSImageServiceImpl implements OSSImageService {

    @Override
    public String upload(MultipartFile file) throws Exception {
        OSSClientUtil ossClient = new OSSClientUtil();
        String name = ossClient.uploadImg2Oss(file);
        String imgUrl = ossClient.getImgUrl(name);
        String[] split = imgUrl.split("\\?");
        if (split[0] != null) {
            try {
                String url = split[0];
                return url.replace("-internal", "");
            } catch (Exception e) {
                return split[0];
            }
        }
        return split[0];

    }

}
