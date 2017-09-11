package com.supermap.lbsp.provider.service.personSign;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.bean.PersonPlanBean;
import com.supermap.lbsp.provider.common.Page;

public interface PersonSignService {
	
	public List<PersonPlanBean> queryPersonSign(Page page, HashMap<String, Object> hm);

	public Page pagePersonSign(Page page,HashMap<String,Object> hm);
}
