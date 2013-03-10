package cz.zcu.kiv.eegdatabase.data.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * User: Jan Koren
 * Date: 14.2.13
 * Time: 16:09
 * To change this template use File | Settings | File Templates.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SolrId {
}
