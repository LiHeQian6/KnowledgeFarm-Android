package com.knowledge_farm;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName Controller
 * @Description
 * @Author 张帅华
 * @Date 2020-04-24 17:54
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @ApiOperation(value = "test方法", notes = "用来测试的方法")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", dataType = "int", paramType = "query", required = false),
        @ApiImplicitParam(name = "name", value = "用户id", dataType = "String", paramType = "query", required = true),
    })
    @PostMapping("/one")
    public String test(@RequestParam("id") Integer id, @RequestParam("name") String name){
        return "This is a test method.";
    }

}
