package org.lufengxue.user.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.lufengxue.enums.StatusCode;
import org.lufengxue.user.service.UserService;
import org.lufengxue.enums.ResponseEnum;
import org.lufengxue.pojo.user.dto.UserDto;
import org.lufengxue.pojo.user.po.UserPo;
import org.lufengxue.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;


/**
 * 作 者: 陆奉学
 * 工 程 名:  elevator
 * 包    名:  org.lufengxue.controller
 * 日    期:  2022-03-2022/3/30
 * 时    间:  0:37
 * 描    述:
 */
@Slf4j
@RestController
@Api(tags = "用户管理")
@RequestMapping("/user")
public class UserControler {


    @Autowired
    private UserService userService;

    /**
     * 新增用户
     *
     * @param
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "新增用户", notes = "新增一个用户的数据")
    public Result insert(UserPo userPo) {
        Integer userNumber = userService.insert(userPo);
        if (userNumber >= 1) {
            return Result.success(ResponseEnum.MISSION_OK);
        } else {
            throw new RuntimeException("新增用户数据失败");

        }
    }

    /**
     * feign调用  根据用户名 查询用户信息
     */
    @GetMapping("/load")
    @ApiOperation(value = "根据用户名 查询用户信息", notes = "feign调用")
    @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", required = true)
    public Result<UserDto> findByName(String username) {
        UserDto user = userService.findByName(username);
        return new Result(StatusCode.OK, "查询用户数据成功", user);
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除用户", notes = "根据用户id删除用户数据")
    @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", required = true)
    public Result deleteId(@RequestParam(name = "username") String username) {
        Integer number = userService.deleteId(username);
        if (number >= 1) {
            return new Result<>(StatusCode.OK, "删除用户数据成功");
        } else {
            throw new RuntimeException("删除用户数据失败");
        }
    }

    /**
     * 查询用户列表
     *
     * @return
     */
    @GetMapping("/findAll")
    @ApiOperation(value = "查询用户列表", notes = "查询所有用户")
    public Result<List<UserDto>> findAll() {
        List<UserDto> userDtoList = userService.findAll();
        return new Result<>(StatusCode.OK, "查询所有用户数据成功", userDtoList);
    }

    /**
     * 更新数据
     */
    @PostMapping("/updateUser")
    @ApiOperation(value = "更新用户列表", notes = "根据用户id更新用户数据")
    public Result updateUser(UserPo userPo) {
        try {
            Integer number = userService.updateUser(userPo);
            return new Result(StatusCode.OK, "更新用户数据成功");
        } catch (Exception e) {
            throw new RuntimeException("更新用户数据失败");
        }
    }


    public static void main(String[] args) throws Exception {
        //bcrypt
        String encode = new BCryptPasswordEncoder().encode("123456");
        System.out.println(encode);

        //
        byte[] bytes = Base64.getDecoder().decode("eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9");
        System.out.println(new String(bytes, "utf-8"));
    }
}
