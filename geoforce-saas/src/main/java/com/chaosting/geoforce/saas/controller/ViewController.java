package com.chaosting.geoforce.saas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

	@RequestMapping(value = "distribute/demo")
	public String index() {
		return "distribute/demo";
	}
}
