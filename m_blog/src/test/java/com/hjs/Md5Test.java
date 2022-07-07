package com.hjs;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.Digester;
import com.hjs.util.PasswordMd5;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Test
    public void test02(){
        String[] strings = {"1","2","3","4","5","1","2","3"};

        //使用List去重
        List<String> list = new ArrayList<>();
        for (int i = 0; i < strings.length; i++) {
            if (!list.contains(strings[i])){
                System.out.println("strings = " + strings[i]);
                list.add(strings[i]);
            }
        }

        System.out.println("list = " + list);
    }

    @Test
    public void test03(){
        // contains方法：用于判断list集合是否包含某个元素
        // containsKey方法：用于判断map键中是否包含某个键
        // containsKeyValues方法：用于判断map中是否包含某个值

        List list = new ArrayList();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");
        if (!list.contains("A")){
            System.out.println("list = " + list);
        }else {
            System.out.println("list = " + list);
        }
        System.out.println("list.contains(\"A\") = " + !list.contains("A"));

        Map map = new HashMap();
        map.put("A","1");
        map.put("B","2");
        map.put("C","3");
        map.put("D","4");
        System.out.println("map.containsKey(\"A\") = " + map.containsKey("A"));
        System.out.println("map.containsValue(\"4\") = " + map.containsValue("4"));
         /*int count = 0;
        for (int i = 1; i < 1000000000; i++) {
            int index = 0;
            //计算位数
            while (i > 0){
                i = i/10;
                index++;
            }
            //逐位判断，有2就计数器加1
            for (int j = 1; j <= index; j++) {
                if (i%(Math.pow(10,i)) == 2){
                    count++;
                }
                //位数上升
                i = i/10;
            }
        }
        System.out.println("count = " + count);*/
    }

    @Test
    public void test04(){
        System.out.println("cal(1000000000) = " + cal(1000000000));
    }

    public static int cal(int number){
        //计数器
        int count = 0;
        for (int i = 0; i <= number; i++) {
            count = cal2(count,i);
        }
        return count;
    }

    //判断该数字有多少个2
    public static int cal2(int count,int temp){
        int number = temp;
        int index = 0;
        //计算位数
        while (number > 0){
            number = number/10;
            index++;
        }
        number = temp;
        //逐位判读，有2就计数器加1
        for (int i = 1; i <= index; i++) {
            if (number%(Math.pow(10,i)) == 2){
                count++;
            }
            //位数上升
            number = number/10;
        }
        return count;
    }
}
