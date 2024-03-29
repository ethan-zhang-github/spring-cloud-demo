package com.just1984.spring.cloud.demo.service.provider.controller;

import com.google.common.collect.Lists;
import com.just1984.spring.cloud.demo.service.api.sdk.ProviderApi;
import com.just1984.spring.cloud.demo.service.api.vo.User;
import com.just1984.spring.cloud.demo.service.provider.service.ProviderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

/**
 * @description:
 * @author: zhangyifan@wshifu.com
 * @date: 2019-08-15 21:18
 */
@Slf4j
@RestController
@RequestMapping("provider")
public class ProviderController implements ProviderApi {

    private static final Random random = new Random();

    @Autowired
    private ProviderService providerService;

    @Override
    public void addUser(@RequestBody User user) {
        providerService.addUser(user);
    }

    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "150")
            },
            fallbackMethod = "fallbackForGetUserList"
    )
    @Override
    public List<User> getUserList() {
        try {
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            log.error("InterruptedException:", e);
        }
        return providerService.getUserList();
    }

    @Override
    public void clear() {
        providerService.clear();
    }

    public List<User> fallbackForGetUserList() {
        return Lists.newArrayList();
    }

}
