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

import cn.enaium.chat.server.entity.UserEntity;
import cn.enaium.chat.server.entity.UserInfoEntity;
import cn.enaium.chat.server.mapper.UserInfoMapper;
import cn.enaium.chat.server.mapper.UserMapper;
import cn.enaium.chat.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Enaium
 */
@Service
public class UserServiceImplement implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public long login(String username, String password) {
        var user = userMapper.getByUsernameAndPassword(username, password);
        return user == null ? -1 : user.getId();
    }

    @Override
    public boolean hasUsername(String username) {
        return userMapper.getByUsername(username) != null;
    }

    @Override
    public void register(String username, String password, String nickname) {
        var userEntity = new UserEntity(null, username, password, new Date());
        userMapper.insert(userEntity);
        userInfoMapper.insert(new UserInfoEntity(userEntity.getId(), nickname, null, null));
    }
}
