package com.example.bookmark.security.util;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.*;

import static com.example.bookmark.security.util.TestDataUtil.USERID_BRUCE_WAYNE;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithMockBookmarkUserSecurityContextFactory.class)
public @interface WithMockBookmarkUser {
    String identifier() default USERID_BRUCE_WAYNE;

    String firstName() default "Bruce";

    String lastName() default "Wayne";

    String email() default "bruce.wayne@example.com";

    String[] roles() default {"USER"};
}
