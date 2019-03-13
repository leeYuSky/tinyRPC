package edu.tju.scs.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author: liyuze
 * @Description:
 * @Date: Created in 13:21 18/5/16.
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RPCService {
    String Interface();
}
