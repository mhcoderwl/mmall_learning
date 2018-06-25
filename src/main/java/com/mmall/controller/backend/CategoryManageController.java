package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import com.mmall.util.CookieUtil;
import com.mmall.util.JedisUtil;
import com.mmall.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.mmall.pojo.User;
import java.util.List;
//后台功能，前端用户无法访问。需要校验当前用户是否是管理员。
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private IUserService userService;
    @Autowired
    private ICategoryService categoryService;
    @RequestMapping(value="/add_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addCategory(HttpServletRequest request, @RequestParam(value="parentID",defaultValue = "0") int parentID,
                                      String categoryName){
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        ServerResponse response=userService.checkAdminRole(user);
        if(!response.isSuccess()){
            return ServerResponse.createByErrorMessage("当前用户非管理员无权操作");
        }
        return categoryService.addCategory(parentID,categoryName);

    }

    @RequestMapping(value="set_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse setCategoryName(HttpServletRequest request, Integer categoryId, String categoryName) {
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        if (userService.checkAdminRole(user).isSuccess()) {
            //更新categoryName
            return categoryService.updateCategoryName(categoryId, categoryName);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }

    @RequestMapping(value="get_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        if (userService.checkAdminRole(user).isSuccess()) {
            //查询子节点的category信息,并且不递归,保持平级
            return categoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }
    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Integer>> getCategoryAndDeepChildrenCategory(HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        String userStr=JedisUtil.get(CookieUtil.readLoginToken(request));
        if(userStr==null){
            return ServerResponse.createByErrorMessage("当前用户未登录");
        }
        User user= JsonUtil.string2Obj(userStr,User.class);
        if (userService.checkAdminRole(user).isSuccess()) {
            //查询当前节点的id和递归子节点的id
//            0->10000->100000
            return categoryService.selectCategoryAndChildrenById(categoryId);

        } else {
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }


}
