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

package com.hpb.bc.entity.result;

public enum ResultCode {
    /**
     * 成功
     */
    SUCCESS("000000", "成功"),
    /**
     * 失败
     */
    FAIL("999999", "失败"),

    /**
     * 网络异常，请稍后重试
     */
    WARN("-1", "网络异常，请稍后重试"),

    /**
     * 没有登录
     */
    NOT_LOGIN("400", "没有登录"),

    /**
     * 发生异常
     */
    EXCEPTION("401", "发生异常"),

    /**
     * 发生异常
     */
    ERROR_EXCEPTION("40101", "获取区块链信息异常"),

    /**
     * 系统错误
     */
    SYS_ERROR("402", "系统错误"),

    /**
     * 参数错误
     */
    PARAMS_ERROR("403", "参数错误 "),

    /**
     * 响应结果为空
     */
    RESPONSE_EMPTY("404", "响应结果为空"),

    /**
     * 响应结果为空
     */
    RESPONSE_NULL("404", "响应结果为空"),

    /**
     * 不支持或已经废弃
     */
    NOT_SUPPORTED("410", "不支持或已经废弃"),

    /**
     * AuthCode错误
     */
    INVALID_AUTHCODE("444", "无效的AuthCode"),

    /**
     * 太频繁的调用
     */
    TOO_FREQUENT("445", "太频繁的调用"),

    /**
     * 未知的错误
     */
    UNKNOWN_ERROR("499", "未知错误"),

    /**
     * 不合法 钱包地址
     */
    INVALID_ADDRESS("10104", "不合法 钱包地址"),

    /**
     * 不合法 交易HASH
     */
    INVALID_TXHASH("10105", "不合法 交易HASH"),

    /**
     * 不合法 区块HASH
     */
    INVALID_BLOCKHASH("10106", "不合法 区块HASH"),

    /**
     * 参数错误
     */
    PARAMETER_ERROR("10101", "参数错误"),

    VALIDATE_FAIL("000001", "合约验证失败");


    private ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    private String code;
    private String msg;
}
