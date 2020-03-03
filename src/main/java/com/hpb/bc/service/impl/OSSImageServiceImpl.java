/*
 * Copyright 2020 HPB Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
