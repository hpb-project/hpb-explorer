package com.hpb.bc.service;

import java.util.Map;

/**
 * @author will
 * api 请求门脸
 */
public interface ApiFacadeService {

    Object doExcute(Map<String, String> map) throws Exception;
}
