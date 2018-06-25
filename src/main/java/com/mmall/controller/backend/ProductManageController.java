package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JedisUtil;
import com.mmall.util.JsonUtil;
import com.mmall.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IProductService productService;
    @Autowired
    private IFileService fileService;
    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse productSave(HttpServletRequest request, Product product){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        if(userService.checkAdminRole(user).isSuccess()){
            //填充我们增加产品的业务逻辑
            return productService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }

    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpServletRequest request, Integer productId,Integer status){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        if(userService.checkAdminRole(user).isSuccess()){
            return productService.setSaleStatus(productId,status);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpServletRequest request, Integer productId){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        if(userService.checkAdminRole(user).isSuccess()){
            //填充业务
            return productService.manageProductDetail(productId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
    @RequestMapping("list.do")
    @ResponseBody
    //展示商品时需要页面大小和页数来展示
    public ServerResponse getList(HttpServletRequest request, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        if(userService.checkAdminRole(user).isSuccess()){
            //填充业务
            return productService.getProductList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse searchProduct(HttpServletRequest request,@RequestParam(value = "productName" ,required = false)String productName,@RequestParam(value="productID" ,required = false)Integer productID,@RequestParam(value = "pageNum",defaultValue = "1") int pageNum, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        if(userService.checkAdminRole(user).isSuccess()){
            //填充业务
            return productService.searchProduct(productName,productID,pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file",required = false)MultipartFile file, HttpServletRequest request){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        if(userService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");//得到upload文件夹在运行时的路径

            String targetFileName = fileService.upload(file, path);
            //拼出完整地址返回给前台
            String url = new StringBuilder().append(PropertiesUtil.getProperty("ftp.server.http.prefix"))
                    .append(targetFileName).toString();
            Map map = Maps.newHashMap();
            map.put("uri", targetFileName);
            map.put("url", url);
            return ServerResponse.createBySuccess(map);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作");
        }
    }
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richTextImgUpload(@RequestParam("upload_file") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));

        User user= JsonUtil.string2Obj(userStr,User.class);
        Map map = Maps.newHashMap();
        if(user == null){
            map.put("msg","未登录");
            map.put("sucess","false");
            return map;
        }
        if(userService.checkAdminRole(user).isSuccess()) {
            String path = request.getSession().getServletContext().getRealPath("upload");//得到upload文件夹在运行时的路径
            String targetFileName = fileService.upload(file, path);
            //拼出完整地址返回给前台
            String url = new StringBuilder().append(PropertiesUtil.getProperty("ftp.server.http.prefix"))
                    .append(targetFileName).toString();
            map.put("msg","上传成功");
            map.put("sucess","true");
            map.put("file_path",url);
            response.addHeader("Access=Control-Allow-Headers","X-File-Name");
            return map;
        }else{
            map.put("msg","无权限操作");
            map.put("sucess","false");
            return map;
        }
    }


}
