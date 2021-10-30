/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.catalina.connector;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;

import org.apache.catalina.Context;
import org.apache.catalina.startup.SimpleHttpClient;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.startup.TomcatBaseTest;

public class TestKeepAliveCount extends TomcatBaseTest {

    @Test
    public void testHttp10() throws Exception {
        TestKeepAliveClient client = new TestKeepAliveClient();
        client.doHttp10Request();
    }

    @Test
    public void testHttp11() throws Exception {
        TestKeepAliveClient client = new TestKeepAliveClient();
        client.doHttp11Request();
    }


    private class TestKeepAliveClient extends SimpleHttpClient {


        private boolean init;

        private synchronized void init() {
            if (init) {
              return;
            }

            Tomcat tomcat = getTomcatInstance();
            Context root = tomcat.addContext("", TEMP_DIR);
            Tomcat.addServlet(root, "Simple", new SimpleServlet());
            root.addServletMappingDecoded("/test", "Simple");
            //这个参数表示一个socket连接最多能处理多少个请求。这里设置的是5个，表示某一个socket最多处理5个请求。
            // 当处理完5个后，tomcat里的这个socket会主动关闭。注意：如果某个socket在connectionTimeout的时间
            // 里没有收到完整的请求，比如先收到某个请求行（一个完整的请求包含请求行，请求头，请求体，请求体好像不是必须的），
            // 之后在connectionTimeout时间段内，一直没有收到请求头。这样就是在等待时间内没有收到完整的请求。
            // 此时即便不到5个，当前socket也会被关闭。之所以关闭应该是tomcat考虑到这个socket没能收到完整的请求，
            // 可能跟这个通道有关，认为是这个通道有问题，所以就直接关闭了。
            Assert.assertTrue(tomcat.getConnector().setProperty("maxKeepAliveRequests", "2"));
            //这个参数表示等待一个请求剩余部分到来的最大时间。比如对于某个socket连接，先发一个请求的请求行，
            // 之后的请求头如果没能在connectionTimeout时间段内发到tomcat，则tomcat会关闭这个socket。
            Assert.assertTrue(tomcat.getConnector().setProperty("connectionTimeout", "2689"));
            //这个参数表示一个socket连接在上一个请求处理完毕后，多久没收到新的请求，就关闭socket。注意，如果处理完
            // 某个请求后，当前socket累计处理的请求数达到maxKeepAliveRequests，则不会等待下一个请求，socket会被直接关闭
            Assert.assertTrue(tomcat.getConnector().setProperty("keepAliveTimeout", "10000"));
            init = true;
        }

        private void doHttp10Request() throws Exception {
            Tomcat tomcat = getTomcatInstance();
            init();
            tomcat.start();
            setPort(tomcat.getConnector().getLocalPort());

            // Open connection
            connect();

            // Send request in two parts
            String[] request = new String[1];
            request[0] =
                "GET /test HTTP/1.0" + CRLF + CRLF;
            setRequest(request);
            processRequest(false); // blocks until response has been read
            boolean passed = (this.readLine()==null);
            // Close the connection
            disconnect();
            reset();
            tomcat.stop();
            Assert.assertTrue(passed);
        }

        private void doHttp11Request() throws Exception {
            Tomcat tomcat = getTomcatInstance();
            init();
            tomcat.start();
            setPort(tomcat.getConnector().getLocalPort());

            // Open connection
            connect();

            // Send request in two parts
            String[] request = new String[2];
            request[0] =
                "GET /test HTTP/1.1" + CRLF +
                    "Host: localhost" + CRLF +
                    "Connection: Keep-Alive" + CRLF+
                    "Keep-Alive: 300"+ CRLF+ CRLF;

            request[1] =
                "GET /test HTTP/1.1" + CRLF +
                    "Host: localhost" + CRLF +
                    "Connection: Keep-Alive" + CRLF+
                    "Keep-Alive: 300"+ CRLF+ CRLF;


            setRequest(request);

            for (int i=0; i<2; i++) {
                processRequest(false); // blocks until response has been read
//                Assert.assertTrue(getResponseLine()!=null && getResponseLine().startsWith("HTTP/1.1 200 "));
                Thread.sleep(1000);
            }
            boolean passed = (this.readLine()==null);
            // Close the connection
            disconnect();
            reset();
            tomcat.stop();
            Assert.assertTrue(passed);
        }

        @Override
        public boolean isResponseBodyOK() {
            return true;
        }

    }


    private static class SimpleServlet extends HttpServlet {

        private static final long serialVersionUID = 1L;

        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.setContentLength(0);
            resp.flushBuffer();
        }

    }

}
