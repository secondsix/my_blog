package com.hjs.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class PasswordMd5 {
    /**
     * MD5加密之方法三
     * @explain springboot自带MD5加密
     * @param str
     *            待加密字符串
     * @return 16进制加密字符串
     */
    public static String encrypt3ToMD5(String str) {
        log.debug("MD5待加密字符串：\n"+str);
        String md5 = "";
        md5 = DigestUtils.md5DigestAsHex(str.getBytes(StandardCharsets.UTF_8));
        log.debug("md5加密结果"+md5);
        return md5;
    }
}
