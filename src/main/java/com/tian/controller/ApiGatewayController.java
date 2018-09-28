package com.tian.controller;

import com.tian.service.IApiGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by tom on 2018/9/28.
 */
@RestController
public class ApiGatewayController {
    @Autowired
    private IApiGatewayService gatewayService;

    @RequestMapping(value={"/**"}, produces={"application/json"})
    public Object dispatchRequest(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response)
    {
        return this.gatewayService.dispatchRequest(map, request, response);
    }
}
