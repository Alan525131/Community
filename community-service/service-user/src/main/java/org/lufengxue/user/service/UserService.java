package org.lufengxue.user.service;

import org.lufengxue.pojo.user.dto.UserDto;
import org.lufengxue.pojo.user.po.UserPo;

import java.util.List;

/**
 * 作 者: 陆奉学
 * 工 程 名:  elevator
 * 包    名:  org.lufengxue.service
 * 日    期:  2022-03-2022/3/30
 * 时    间:  1:00
 * 描    述:
 */
public interface UserService {

    UserDto findByName(String username);

    Integer insert(UserPo userPo);

    Integer deleteId(String username);

    List<UserDto> findAll();

    Integer updateUser(UserPo userPo);
}
