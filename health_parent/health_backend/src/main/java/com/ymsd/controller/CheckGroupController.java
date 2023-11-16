package com.ymsd.controller;

import com.ymsd.constant.MessageConstant;
import com.ymsd.entity.PageResult;
import com.ymsd.entity.QueryPageBean;
import com.ymsd.entity.Result;
import com.ymsd.pojo.CheckGroup;
import com.ymsd.pojo.CheckItem;
import com.ymsd.service.ICheckGroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Resource
    private ICheckGroupService checkGroupService;


    @RequestMapping("/addCheckGroup/{ids}")
    public Result addCheckGroup(@PathVariable("ids")Integer[] checkItem,@RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.addCheckGroup(checkItem,checkGroup);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable("id")Integer checkGroupId){
        try {
            checkGroupService.deleteById(checkGroupId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    @PutMapping("/updateCheckGroup/{checkitemIds}")
    public Result updateCheckGroup(@PathVariable("checkitemIds")Integer[] checkItem,@RequestBody CheckGroup checkGroup){
        try {
            checkGroupService.updateCheckGroup(checkItem,checkGroup);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }
    @GetMapping("/findByCheckGroup/{id}")
    public Result findByCheckGroup(@PathVariable("id")Integer checkGroupId){
        try {
            Integer[] checkItemIds = checkGroupService.findByCheckGroup(checkGroupId);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_FAIL);

        }
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) throws Exception{
        PageResult pageResult = checkGroupService.findPage(queryPageBean);
        return pageResult;
    }

    @GetMapping("/findAll")
    public Result findAll(){
        List<CheckGroup> checkGroups = null;
        try {
            checkGroups  = checkGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }


    }
}
