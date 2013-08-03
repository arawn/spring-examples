package spring.examples.core.mybatis;

import java.lang.annotation.*;

/**
 * MyBatis Mapper Marker Annotation
 *
 * {@link org.mybatis.spring.mapper.MapperScannerConfigurer}에 의해서 Mapper Bean으로 등록되어야하는 대상으로 사용
 *
 * @author arawn.kr@gmail.com
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapper {
}
