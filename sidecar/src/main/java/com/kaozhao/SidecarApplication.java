package com.kaozhao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.sidecar.EnableSidecar;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableSidecar
@SpringBootApplication
@EnableZuulProxy
public class SidecarApplication {
	public static void main(String[] args) {
		SpringApplication.run(SidecarApplication.class, args);
	}
}
