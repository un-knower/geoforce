package com.dituhui.openapi.portal.controller;

import com.dituhui.openapi.entity.PortalUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    //@RequestMapping(value = "/index")
    //public String index() {
    //    return "index";
    //}

    @RequestMapping(value = "/findPassword")
    public String findPassword() {
        return "findPassword";
    }

    @RequestMapping(value = "/register")
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/editEmail")
    public String editEmail() {
        return "editEmail";
    }

    @RequestMapping(value = "/editPassword")
    public String editPassword() {
        return "editPassword";
    }

    @RequestMapping(value = "/console")
    public String console() {
        return "console";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/developmentDoc")
    public String developmentDoc() {
        return "developmentDoc";
    }

    @RequestMapping(value = "/faq")
    public String faq() {
        return "faq";
    }

    @RequestMapping(value = "/permission")
    public String permission() {
        return "permission";
    }

    @RequestMapping(value = "/consolePerson")
    public String consolePerson() {
        return "consolePerson";
    }

    @RequestMapping(value = "/contact")
    public String contact() {
        return "contact";
    }

    //@RequestMapping(value = "/feedback")
    //public String feedback() {
    //    return "feedback";
    //}

    @RequestMapping(value = "/notice")
    public String notice() {
        return "notice";
    }

    @RequestMapping(value = "/saleSupport")
    public String saleSupport() {
        return "saleSupport";
    }

    @RequestMapping(value = "/about")
    public String about() {
        return "about";
    }

    @RequestMapping(value = "/term")
    public String term() {
        return "term";
    }

    @RequestMapping(value = "/products")
    public String products() {
        return "products";
    }


    @RequestMapping(value = "/feedbacktest")
    public String feedbacktest() {
        return "feedbackTest";
    }

    //可能配置访问路径
    @RequestMapping("/")
    public String ind() {
        return "index";
    }
    @RequestMapping("index")
    public String index() {
        return "index";
    }

    @RequestMapping("feedback")
    public String feedback() {
        return "feedback";
    }

    @RequestMapping("news_list")
    public String news_list() {
        return "news_list";
    }

    @RequestMapping("news_detail")
    public String news_detail() {
        return "news_detail";
    }

    @RequestMapping("product")
    public String product() {
        return "product";
    }

    @RequestMapping("product_limit")
    public String product_limit() {
        return "product_limit";
    }

    @RequestMapping("support_errorCode")
    public String support_errorCode() {
        return "support_errorCode";
    }

    @RequestMapping("support_develop")
    public String support_develop() {
        return "support_develop";
    }
    @RequestMapping("FAQ")
    public String FAQ() {
        return "FAQ";
    }

    // 模拟登陆
    @RequestMapping("u/login")
    public String uLogin() {
        PortalUser pUser = new PortalUser();
        pUser.setPhone("15828030050");
        pUser.setPassword("111111");
        UsernamePasswordToken upt = new UsernamePasswordToken(pUser.getPhone(), pUser.getPassword());
        Subject subject = SecurityUtils.getSubject();
        subject.login(upt);
        return "redirect:/console";
    }

    @RequestMapping(value = "distribute/demo")
	public String demo() {
		return "distribute/demo";
	}
	
	@RequestMapping(value = "distribute/free")
	public String free() {
		return "distribute/free";
	}
	
	@RequestMapping("cando")
    public String cando() {
        return "cando";
    }
}
