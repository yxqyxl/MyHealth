package com.ymsd.controller;

import com.ymsd.constant.MessageConstant;
import com.ymsd.entity.PageResult;
import com.ymsd.entity.QueryPageBean;
import com.ymsd.entity.Result;
import com.ymsd.pojo.Setmeal;
import com.ymsd.service.ISetMealService;
import com.ymsd.utils.QiniuUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Resource
    private ISetMealService setMealService;

    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) throws  Exception{
        try {
            String filename = imgFile.getOriginalFilename();
            filename = UUID.randomUUID().toString() + filename;
            byte[] imgFileByte = imgFile.getBytes();
            QiniuUtils.upload2Qiniu(imgFileByte,filename);
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,filename);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.PIC_UPLOAD_FAIL);

        }
    }

    @RequestMapping("/addSetmeal/{ids}")
    public Result addSetmeal(@PathVariable("ids")Integer[] checkGroupIds, @RequestBody Setmeal setmeal){
        try {
            setMealService.addSetmeal(checkGroupIds,setmeal);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }


    }



    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = setMealService.findPage(queryPageBean);
        return pageResult;
    }

//    @DeleteMapping("/deleteById/{id}&{filename}")
//    public Result deleteById(@PathVariable("id")Integer setmealId,@PathVariable("filename")String filename){
//        try {
//            setMealService.deleteById(setmealId,filename);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
//        }
//        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
//    }


    /**
     *      delete不是正真意义的删除，修改status=0字段实现隐藏显示，后续需要使用再上架
     * @param setmealId    隐藏的id
     * @return  Result
     */
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable("id")Integer setmealId){
        try {
            setMealService.deleteById(setmealId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }

    @GetMapping("/findBySetmeal/{id}")
    public Result findBySetmealId(@PathVariable("id")Integer setmealId){
        try {
            Integer[] setmealIds = setMealService.findBySetmealId(setmealId);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmealIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_SETMEAL_FAIL);

        }
    }

    @PutMapping("/updateSetmeal/{checkgroupIds}")
    public Result updateSetmeal(@PathVariable("checkgroupIds")Integer[] checkgroupIds,@RequestBody Setmeal setmeal){
        try {
            setMealService.updateSetmeal(checkgroupIds,setmeal);

            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }





}
