package cz.zcu.kiv.eegdatabase.data.annotation;

import cz.zcu.kiv.eegdatabase.data.indexing.IndexField;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 13.2.13
 * Time: 22:54
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SolrField {
    IndexField name();
}
