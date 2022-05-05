package org.lufengxue.user.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.lufengxue.pojo.user.dto.UserDto;
import org.lufengxue.pojo.user.po.UserPo;

import java.util.List;

/**
 * 作 者: 陆奉学
 * 工 程 名:  elevator
 * 包    名:  org.lufengxue.user.mapper
 * 日    期:  2022-03-2022/3/30
 * 时    间:  1:11
 * 描    述:
 */
@Mapper
public interface UserMapper {
    /**
     *
     * @param username 用户账号名
     * @return
     */
    UserDto findByName(String username);

    /**
     *  新增用户
     * @param userPo 用户数据参数
     * @return
     */
    Integer insert(UserPo userPo);

    /**
     *  根据用户账户名 删除用户
     * @param username  用户账户名
     * @return
     */
    Integer deleteId(String username);

    /**
     *  查询所有用户
     * @return
     */
    List<UserDto> findAll();

    /**
     *  更新用户数据
     * @param userPo 用户数据参数
     * @return
     */
    Integer updateUser(UserPo userPo);
}
