package com.supermap.egispweb.action;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("addressCorrection")
public class AddressCorrectionAction {
	@RequestMapping("show")
	public String getAddressMatch(){
		return "addressCorrection";
	}
}
