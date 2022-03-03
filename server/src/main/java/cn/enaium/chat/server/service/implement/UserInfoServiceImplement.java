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

package cn.enaium.chat.server.service.implement;

import cn.enaium.chat.server.mapper.UserInfoMapper;
import cn.enaium.chat.server.service.UserInfoService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Enaium
 */
@Service
public class UserInfoServiceImplement implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public LinkedHashSet<Long> getFriend(Long id) {
        LinkedHashSet<Long> friend = new LinkedHashSet<>();
        new Gson().fromJson(userInfoMapper.getById(id).getFriend(), JsonArray.class).forEach(it -> friend.add(it.getAsLong()));
        return friend;
    }

    @Override
    public LinkedHashSet<Long> getGroup(Long id) {
        LinkedHashSet<Long> group = new LinkedHashSet<>();
        new Gson().fromJson(userInfoMapper.getById(id).getGroup(), JsonArray.class).forEach(it -> group.add(it.getAsLong()));
        return group;
    }

    @Override
    public void setFriendById(Long id, Set<Long> friend) {
        userInfoMapper.updateFriendById(id, new Gson().toJson(friend));
    }

    @Override
    public void setGroupById(Long id, Set<Long> group) {
        userInfoMapper.updateGroupById(id, new Gson().toJson(group));
    }
}
