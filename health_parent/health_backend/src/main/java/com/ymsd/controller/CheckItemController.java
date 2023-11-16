package com.ymsd.controller;

import com.ymsd.constant.MessageConstant;
import com.ymsd.pojo.CheckItem;
import com.ymsd.entity.QueryPageBean;
import com.ymsd.entity.PageResult;
import com.ymsd.entity.Result;
import com.ymsd.service.ICheckItemService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/checkItem")
public class CheckItemController {

    @Resource
    private ICheckItemService checkItemService;

    /**
     * 新增检查项
     * @param checkItem
     * @return
     */
    @RequestMapping("/addCheckItem")
    @ApiOperation(value = "添加检查项")
    public Result addCheckItem(@RequestBody CheckItem checkItem){
        try {
            checkItemService.addCheckItem(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findPage")
    @ApiOperation(value = "分页查询")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) throws Exception{
        PageResult pageResult = checkItemService.findPage(queryPageBean);
        return pageResult;
    }


    @DeleteMapping("/deleteById/{id}")
    @ApiOperation(value = "删除检查项")
    public Result deleteById(@PathVariable("id")Integer checkItemId){
        try {
            checkItemService.deleteById(checkItemId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @PutMapping("/updateCheckItem")
    public Result updateCheckItem(@RequestBody CheckItem checkItem){
        try {
            checkItemService.updateCheckItem(checkItem);
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    @GetMapping("/findAll")
    public Result findAll(){
        List<CheckItem> checkItems = null;
        try {
            checkItems  = checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItems);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }


    }
}
