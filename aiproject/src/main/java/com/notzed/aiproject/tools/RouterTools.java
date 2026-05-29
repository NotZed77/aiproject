package com.notzed.aiproject.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class RouterTools {

    @Tool(description = "Reboots the router")
    public String rebootRouter(String serialNo){
        return String.format("Router is restarting with S.No: %s ",serialNo);
    }
}
