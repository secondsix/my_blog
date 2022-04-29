package com.hjs;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import com.hjs.util.PasswordMd5;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Md5Test {

    @Test
    public void test01(){
        String str = "123";
        String s = PasswordMd5.encrypt3ToMD5(str);
        System.out.println("s = " + s);

        String s1 = SecureUtil.md5(str);
        System.out.println("s1 = " + s1);

        String s2 = SecureUtil.md5(s1);
        System.out.println("s2 = " + s2);

    }

    @Test
    public void md5() {
        String text = "123";

        // 第一种：创建Digester对象，执行加密
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String digestHex = md5.digestHex(text);
        System.out.println(digestHex);

        // 第二种：执行使用DigestUtil
        String md5Hex = DigestUtil.md5Hex(text);
        System.out.println(md5Hex);
    }

    @Test
    public void sha1() {
        String text = "HelloWorld";

        // 第一种：创建Digester对象，执行加密
        Digester md5 = new Digester(DigestAlgorithm.SHA1);
        String digestHex = md5.digestHex(text);
        System.out.println(digestHex);

        // 第二种：执行使用DigestUtil
        String md5Hex = DigestUtil.sha1Hex(text);
        System.out.println(md5Hex);
    }
}
