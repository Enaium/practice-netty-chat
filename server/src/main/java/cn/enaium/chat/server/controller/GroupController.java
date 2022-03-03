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
import cn.enaium.chat.server.entity.GroupEntity;
import cn.enaium.chat.server.mapper.GroupMapper;
import cn.enaium.chat.server.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Enaium
 */
@Controller
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private GroupService groupService;

    @RequestMapping("/getMember/{id}")
    @ResponseBody
    private List<InfoVO> getMember(@PathVariable("id") Long id) {
        List<InfoVO> groupList = new ArrayList<>();
        var member = groupService.getMember(id);
        for (Long aLong : member) {
            groupList.add(new InfoVO(aLong, groupMapper.getById(aLong).getName(), InfoVO.Type.GROUP));
        }
        return groupList;
    }

    @GetMapping("/query/{name}")
    @ResponseBody
    public List<InfoVO> query(@PathVariable("name") String name) {
        List<InfoVO> userInfoList = new ArrayList<>();
        for (GroupEntity groupEntity : groupMapper.queryByName(name)) {
            userInfoList.add(new InfoVO(groupEntity.getId(), groupEntity.getName(), InfoVO.Type.GROUP));
        }
        return userInfoList;
    }
}
