package org.lufengxue.user.service.impl;

import org.lufengxue.user.mapper.UserMapper;
import org.lufengxue.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.lufengxue.pojo.user.dto.UserDto;
import org.lufengxue.pojo.user.po.UserPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 作 者: 陆奉学
 * 工 程 名:  elevator
 * 包    名:  org.lufengxue.user.service.impl
 * 日    期:  2022-03-2022/3/31
 * 时    间:  14:58
 * 描    述:
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    /**
     * 根据用户名查询数据
     * @param username
     * @return
     */
    @Override
    public UserDto findByName(String username) {
        return userMapper.findByName(username);
    }

    /**
     * 新增用户
     * @param userPo
     */
    @Override
    public Integer  insert(UserPo userPo) {
        String username = userPo.getUsername();
        if (StringUtils.isEmpty(username) || username.length() < 2 || username.length() > 12) {

            throw new RuntimeException("您的输入有误");
        }
        String phone = userPo.getPhone();
        if (StringUtils.isEmpty(phone) || phone.length() != 11 || isNotNumb(phone)) {
            throw new RuntimeException("您输入的电话号码有误");
        }

        Date createDate = new Date();
        userPo.setCreatedDate(createDate);
        userPo.setUpdatedDate(createDate);

        return userMapper.insert(userPo);
    }

    public boolean isNotNumb(String numb){
        for (int i = 1; i < numb.length(); i++) {
            char c = numb.charAt(i);
            if ('0' > c || c > '9') {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据用户 id删除用户数据
     * @param username 用户账号
     * @return
     */
    @Override
    public Integer deleteId(String username) {
        return userMapper.deleteId(username);
    }

    /**
     * 查询用户列表
     * @return
     */
    @Override
    public List<UserDto> findAll() {

        return userMapper.findAll();
    }

    /**
     * 更新数据
     * @param userPo
     * @return
     */
    @Override
    public Integer updateUser(UserPo userPo) {
        return userMapper.updateUser(userPo);
    }

}
