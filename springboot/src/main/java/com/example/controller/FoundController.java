package com.example.controller;

import com.example.common.Result;
import com.example.entity.Found;
import com.example.service.FoundService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 招领信息表前端操作接口
 **/
@RestController
@RequestMapping("/found")
public class FoundController {

    @Resource
    private FoundService foundService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Found found) {
        foundService.add(found);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        foundService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        foundService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Found found) {
        foundService.updateById(found);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Found found = foundService.selectById(id);
        return Result.success(found);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Found found ) {
        List<Found> list = foundService.selectAll(found);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Found found,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Found> page = foundService.selectPage(found, pageNum, pageSize);
        return Result.success(page);
    }

    //首页动态找到最新的四个失物
    @GetMapping("/selectNew4")
    public Result selectNew4(){
        List<Found> list = foundService.selectNew4(); //找到四条数据，所以是有返回值的
        return Result.success(list); //要把找到的四条数据返回到首页
    }

}