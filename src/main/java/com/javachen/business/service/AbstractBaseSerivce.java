package com.javachen.business.service;

import com.javachen.business.helper.ExampleMatcherHelper;
import com.javachen.data.PageResponse;
import com.javachen.data.repository.BaseRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class AbstractBaseSerivce<T, ID> implements BaseService<T, ID> {
    public List<T> findAll(T t) {
        return getRepository().findAll(Example.of(t, ExampleMatcherHelper.and()));
    }

    public PageResponse<List<T>> findPage(T t, Pageable pageable) {
        Page result = getRepository().findAll(Example.of(t, ExampleMatcherHelper.and()), pageable);
        return new PageResponse<List<T>>(result.getTotalElements(), result.getContent());
    }

    public T findById(ID id) {
        return getRepository().findById(id).orElse(null);
    }

    public T add(T t) {
        return getRepository().save(t);
    }

    public T update(T t) {
        return getRepository().saveAndFlush(t);
    }

    public void delete(ID id) {
        getRepository().deleteById(id);
    }

    public abstract BaseRepository<T, ID> getRepository();
}

