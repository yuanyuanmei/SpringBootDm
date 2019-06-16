package com.example;

import com.example.common.util.ExcelUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DmApplicationTests {

    @Test
    public void contextLoads() {
        String[] headers = new String[]{"a","b","c"};
        List<Object[]> list = new ArrayList<>();
        String[] s1 = new String[]{"1","2","3"};
        String[] s2 = new String[]{"4","5","6"};
        String[] s3 = new String[]{"7","8","9"};
        String[] s4 = new String[]{"10","11","12"};
        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);
        ExcelUtil.excelExport("ddd",headers,list,null);

    }

}
