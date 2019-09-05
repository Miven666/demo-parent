package com.miven.component;

import com.hccake.simpleredis.core.OpType;
import com.hccake.simpleredis.hash.CacheForHash;
import com.hccake.simpleredis.string.CacheForString;
import org.springframework.stereotype.Component;

/**
 * @author mingzhi.xie
 * @date 2019/9/4
 * @since 1.0
 */
@Component
public class AspectRedisOperationComponent {

    @CacheForString(type = OpType.CACHED, key ="one book" )
    public String getBook(String isbn) {
        return  "One book and isbn: " + isbn;
    }

    @CacheForHash(type = OpType.CACHED, key = "one fruit", field = "#p0")
    public String getFruit(long count) {
        return  "One fruit and count: " + count;
    }

}
