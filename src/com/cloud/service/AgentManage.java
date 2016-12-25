package com.cloud.service;

import java.util.List;

import com.cloud.config.LoadAgentConfig;

public interface AgentManage {
	List<LoadAgentConfig> getAllAgent(String user);
	
	
}
