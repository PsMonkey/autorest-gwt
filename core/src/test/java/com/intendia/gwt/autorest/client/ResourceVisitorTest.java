package com.intendia.gwt.autorest.client;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.function.Supplier;
import org.junit.Test;

public class ResourceVisitorTest {
    @Test public void collector_visitor_works() {
        Supplier<ResourceVisitor> ch0 = () -> new MyCollectorResourceVisitor().path("http://base");
        MyCollectorResourceVisitor rb0 = (MyCollectorResourceVisitor) ch0.get();
        assertThat(rb0.uri(), equalTo("http://base"));

        Supplier<ResourceVisitor> ch1 = () -> ch0.get().path("path1").param("p1", "v1");
        MyCollectorResourceVisitor rb1 = (MyCollectorResourceVisitor) ch1.get();
        assertThat(rb1.uri(), equalTo("http://base/path1?p1=v1"));

        Supplier<ResourceVisitor> ch2 = () -> ch1.get().path("path2").param("p2", "v2");
        MyCollectorResourceVisitor rb2 = (MyCollectorResourceVisitor) ch2.get();
        assertThat(rb2.uri(), equalTo("http://base/path1/path2?p1=v1&p2=v2"));
    }
    private static class MyCollectorResourceVisitor extends CollectorResourceVisitor {
        protected String query() {
            String query = "";
            for (Param param : params) query += (query.isEmpty() ? "" : "&") + param.key + "=" + param.value;
            return query.isEmpty() ? "" : "?" + query;
        }

        public String uri() {
            String uri = "";
            for (String path : paths) uri += path;
            return uri + query();
        }

        @Override public <T> T as(Class<? super T> container, Class<?> type) {
            return null;
        }
    }
}
