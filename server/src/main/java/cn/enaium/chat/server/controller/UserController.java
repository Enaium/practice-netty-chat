/**
 * Copyright (c) 2022 Enaium
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.enaium.chat.server.controller;

import cn.enaium.chat.common.vo.InfoVO;
import cn.enaium.chat.server.entity.UserInfoEntity;
import cn.enaium.chat.server.mapper.GroupMapper;
import cn.enaium.chat.server.mapper.UserInfoMapper;
import cn.enaium.chat.server.netty.Client;
import cn.enaium.chat.server.netty.NettyServer;
import cn.enaium.chat.server.service.GroupService;
import cn.enaium.chat.server.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Enaium
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private NettyServer nettyServer;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupMapper groupMapper;

    @GetMapping("/getOnline")
    @ResponseBody
    public List<InfoVO> getOnline() {
        List<InfoVO> userInfoList = new ArrayList<>();
        var values = nettyServer.online.values();
        for (Client value : values) {
            if (userInfoMapper.getById(value.getId()) != null) {
                userInfoList.add(new InfoVO(value.getId(), userInfoMapper.getById(value.getId()).getNickname(), InfoVO.Type.USER));
            }
        }
        return userInfoList;
    }

    @GetMapping("/query/{nickname}")
    @ResponseBody
    public List<InfoVO> query(@PathVariable("nickname") String nickname) {
        List<InfoVO> userInfoList = new ArrayList<>();
        for (UserInfoEntity userInfoEntity : userInfoMapper.queryByUsername(nickname)) {
            userInfoList.add(new InfoVO(userInfoEntity.getId(), userInfoEntity.getNickname(), InfoVO.Type.USER));
        }
        return userInfoList;
    }

    @GetMapping("/getFriend/{id}")
    @ResponseBody
    public List<InfoVO> getFriend(@PathVariable("id") Long id) {
        List<InfoVO> userInfoList = new ArrayList<>();
        for (Long aLong : userInfoService.getFriend(id)) {
            var byId = userInfoMapper.getById(aLong);
            userInfoList.add(new InfoVO(byId.getId(), byId.getNickname(), InfoVO.Type.USER));
        }
        return userInfoList;
    }


    @GetMapping("/getGroup/{id}")
    @ResponseBody
    private List<InfoVO> getGroup(@PathVariable("id") Long id) {
        List<InfoVO> userInfoList = new ArrayList<>();
        for (Long groupId : userInfoService.getGroup(id)) {
            for (Long userId : groupService.getMember(groupId)) {
                if (Objects.equals(userId, id)) {
                    var byId = groupMapper.getById(groupId);
                    userInfoList.add(new InfoVO(byId.getId(), byId.getName(), InfoVO.Type.GROUP));
                }
            }
        }
        return userInfoList;
    }


    @GetMapping("/isOnline/{id}")
    @ResponseBody
    public boolean isOnline(@PathVariable("id") Long id) {
        return nettyServer.onlineId.containsKey(id);
    }

    @GetMapping("/getNickname/{id}")
    @ResponseBody
    public String getNickname(@PathVariable("id") Long id) {
        return userInfoMapper.getById(id).getNickname();
    }
}
