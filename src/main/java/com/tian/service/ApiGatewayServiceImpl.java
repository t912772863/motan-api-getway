package com.tian.service;

import com.alibaba.fastjson.JSON;
import com.tian.bean.ClassHolder;
import com.tian.handler.CtClassHandler;
import com.weibo.api.motan.config.BasicRefererInterfaceConfig;
import com.weibo.api.motan.config.ProtocolConfig;
import com.weibo.api.motan.config.RegistryConfig;
import com.weibo.api.motan.config.springsupport.RefererConfigBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * Created by tom on 2018/9/28.
 *
 */
@Service
public class ApiGatewayServiceImpl implements IApiGatewayService {
    private static final Logger logger = LoggerFactory.getLogger(ApiGatewayServiceImpl.class);

    @Autowired
    private ProtocolConfig protocolConfig;

    @Autowired
    private RegistryConfig registryConfig;

    @Autowired
    private BasicRefererInterfaceConfig basicRefererInterfaceConfig;

    @Override
    public Object dispatchRequest(Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
        ClassHolder holder = parseClassHolder(request);
        logger.info("ClassHolder={}", holder);
        String paramString = JSON.toJSONString(paramMap);
        CtClassHandler handler = new CtClassHandler();
        handler.setClassHolder(holder);
        Class interfaceClass = handler.makeInterface();

        RefererConfigBean refererConfig = new RefererConfigBean();
        refererConfig.setProtocol(this.protocolConfig);
        refererConfig.setRegistry(this.registryConfig);
        refererConfig.setBasicReferer(this.basicRefererInterfaceConfig);
        refererConfig.setInterface(interfaceClass);
        Thread.currentThread().setContextClassLoader(interfaceClass.getClassLoader());
        Object referer = refererConfig.getRef();

        Object result = handler.invoke(referer, paramString);
        logger.info("params={}", paramString);
        logger.info("result={}", result);
        return result == null ? "" : result;
    }

    private ClassHolder parseClassHolder(HttpServletRequest request) {
        String uri = request.getRequestURI();
        uri = uri.replaceAll("/", "\\.");
        ClassHolder classHolder = new ClassHolder();
        classHolder.setClassPath(uri.substring(1, uri.lastIndexOf(".")));
        classHolder.setMethodName(uri.substring(uri.lastIndexOf(".") + 1));
        return classHolder;
    }
}
