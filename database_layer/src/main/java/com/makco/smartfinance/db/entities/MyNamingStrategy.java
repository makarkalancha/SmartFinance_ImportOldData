package com.makco.smartfinance.db.entities;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * Created by mcalancea on 2016-03-23.
 */
public class MyNamingStrategy extends org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return new Identifier("{h-schema}" + name.getText(), name.isQuoted());
    }
}
