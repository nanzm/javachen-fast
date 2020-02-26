package com.javachen.business.service;

import com.javachen.data.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<T, ID> {
    public List<T> findAll(T t);

    public PageResponse<List<T>> findPage(T t, Pageable pageable) ;

    public T findById(ID id);

    public T add(T t);

    public T update(T t);

    public void delete(ID id);

}
