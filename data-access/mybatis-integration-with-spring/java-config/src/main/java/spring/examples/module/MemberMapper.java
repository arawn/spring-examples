package spring.examples.module;

import spring.examples.core.mybatis.Mapper;
import spring.examples.model.Member;

/**
 * {@link Member} 모델의 Data Access를 담당하는 마이바티스 매퍼 인터페이스
 *
 * 마이바티스 매퍼 XML[src/main/resources/META-INF/mybatis/mappers/MemberMapper.xml]과 연결된다.
 *
 * @author arawn.kr@gmail.com
 */
@Mapper
public interface MemberMapper {

    Member findById(long id);

    void insert(Member member);

}