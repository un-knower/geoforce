package com.dituhui.openapi.portal.controller;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public abstract class CommonController<N, T> {

    public abstract Map<String, Object> save(N n, MultipartFile[] imageFile);

    public abstract Map<String, Object> delete(T id);

    public abstract Map<String, Object> update(N n);

    public abstract Map<String, Object> query(int page, int size);

}
