package com.hisign.xingzhen.config;

import cn.jmessage.api.JMessageClient;
import cn.jmessage.api.user.UserClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;

/**
 * Created by hisign on 2017/10/26.
 */
@Configuration
public class JMConfig {

    private String appkey="a15c1e9bb38c1607b9571eea";

    private String masterSecret="bd4d826e1e49340aac2d05e2";

    @Bean(name = "jMessageClient")
    public JMessageClient jMessageClient() throws SQLException {
        JMessageClient client = new JMessageClient(appkey, masterSecret);
        return client;
    }

    @Bean(name = "userClient")
    public UserClient userClient(){
        return new UserClient(appkey,masterSecret);
    }
}
