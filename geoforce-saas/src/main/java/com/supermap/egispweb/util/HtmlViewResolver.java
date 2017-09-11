package com.supermap.egispweb.util;

import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class HtmlViewResolver extends UrlBasedViewResolver{

	@SuppressWarnings("rawtypes")
	public HtmlViewResolver() {
		Class viewClass = requiredViewClass();
		setViewClass(viewClass);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.UrlBasedViewResolver#requiredViewClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Class requiredViewClass() {
		return HtmlView.class;
	}

}

