package com.xunxiang.xunxiangwikibase;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
@MapperScan("com.xunxiang.xunxiangwikibase.mapper")
public class XunxiangwikibaseApplication {

	private static final Logger LOG = LoggerFactory.getLogger(XunxiangwikibaseApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(XunxiangwikibaseApplication.class);
		Environment environment = app.run(args).getEnvironment();
		LOG.info("Start Successful!");
		LOG.info("Address: \thttp://127.0.0.1:{}", environment.getProperty("server.port"));

	}
}
