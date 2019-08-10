package com.example.wxdemo.controller.wechat;

import com.example.wxdemo.utils.Utils;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author lqx
 * @create 2018-08-09 16:45
 */
@RestController
@RequestMapping("/gongneng")
public class WechatGongNengController {


    /**
     * 上传
     * @param desc
     * @param upfile
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> uploadFile(String desc, @RequestParam(value = "upfile", required = false) MultipartFile upfile, HttpServletRequest request, HttpServletResponse response) {
        System.out.println(request.getParameter("user"));
        Map<String, String> map = new HashMap<>();
        String fileName=upfile.getOriginalFilename();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String filename = sdf.format(new Date()) + new Random().nextInt(1000);
        String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        filename=filename+"."+fileExt;//存入虚拟目录后的文件名
        String saveUrl=Utils.getProperties("save_path");
        File uploadedFile = new File(saveUrl+"\\img", filename);//存入虚拟目录后的文件
        try {
            upfile.transferTo(uploadedFile);//上传
            map.put("url", "/img/"+filename);//这个url是前台回显路径（回显路径为config.json中的imageUrlPrefix+此处的url）
            map.put("state", "SUCCESS");
            map.put("title", filename);
            map.put("original", filename);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("------------------");
        return map;
    }




}
