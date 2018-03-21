package com.example.demo.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.spi.MetadataBuildingContext;

/**
 * 配置 jpa 的字段命名 ,,.没注意看到每个表的前缀都不同，所以没用了
 */
public class RoncooNameing extends ImplicitNamingStrategyJpaCompliantImpl {
    @Override
    protected Identifier toIdentifier(String stringForm, MetadataBuildingContext buildingContext) {
        return super.toIdentifier("e_" + stringForm, buildingContext);
    }


}
