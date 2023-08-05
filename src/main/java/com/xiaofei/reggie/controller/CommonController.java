package com.xiaofei.reggie.controller;


import com.xiaofei.reggie.common.R;
import com.xiaofei.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> fileUpload(MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除1og.info(file.toString()) ;
        //原始文件名
        String originalFilename = file.getOriginalFilename() ; // abc. jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf ( "."));
        //使用UUID重新生成文件名，防止文件名称重复造成文件覆盖
        String fileName = UUID.randomUUID().toString() + suffix; // dfsdfdfd.jpg
        //创建一个目录对象
        File dir = new File(basePath);//判断当前目录是否存在
        if(!dir.exists()){
            //目录不存在,需要创建dir.mkdirs();
            dir.mkdirs();
        }
        try {
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + fileName)) ;
        } catch (IOException e){
            e.printStackTrace () ;
        }
        return R.success(fileName);
    }
    //给浏览器响应图片
    @GetMapping("/download")
    public void fileDownload(String name, HttpServletResponse response){
        try {
            FileInputStream fis = new FileInputStream(new File(basePath + name));
            byte[] bytes = new byte[1024];
            int len = 0;
            ServletOutputStream fos = response.getOutputStream();
            while ((len = fis.read(bytes)) != -1){//从输入流读取
                fos.write(bytes);//写入输出流
                fos.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
