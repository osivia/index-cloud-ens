package org.osivia.demo.directory.service;

import org.osivia.demo.directory.model.CustomizedPerson;
import org.osivia.directory.v2.service.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Person service customized implementation.
 * 
 * @author Cédric Krommenhoek
 * @see PersonServiceImpl
 * @see CustomizedPersonService
 */
@Service
@Primary
public class CustomizedPersonServiceImpl extends PersonServiceImpl implements CustomizedPersonService {

    /** Application context. */
    @Autowired
    protected ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public CustomizedPersonServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public CustomizedPerson getEmptyPerson() {
        return this.applicationContext.getBean(CustomizedPerson.class);
    }

}
