package shell.testsshshell3ok;/*
 * #%L
 * ExpectIt
 * %%
 * Copyright (C) 2014 Alexey Gavrilov and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;

import java.io.IOException;
import java.util.Properties;

import static net.sf.expectit.filter.Filters.removeColors;
import static net.sf.expectit.filter.Filters.removeNonPrintable;
import static net.sf.expectit.matcher.Matchers.contains;
import static net.sf.expectit.matcher.Matchers.regexp;
import static net.sf.expectit.matcher.Matchers.sequence;

/**
 * An example of interacting with SSH server
 */
public class SSHClientMyDockerApp_ok {
    public static void main(String[] args) throws JSchException, IOException {
        JSch jSch = new JSch();
        Session session = jSch.getSession("docker", "192.168.99.100");
        session.setPassword("tcuser");
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();
        Channel channel = session.openChannel("shell");
        channel.connect();

        Expect expect = new ExpectBuilder()
                .withOutput(channel.getOutputStream())
                .withInputs(channel.getInputStream(), channel.getExtInputStream())
                .withEchoInput(System.out)
                .withEchoOutput(System.err)
             ///   .withInputFilters(removeColors(), removeNonPrintable())
                .withExceptionOnFailure()
                .build();

//        Expect expect = new ExpectBuilder()
//                .withOutput(channel.getOutputStream())
//                .withInputs(channel.getInputStream(), channel.getExtInputStream())
//                .withEchoInput(new PrintStream(System.out) {
//                    @Override
//                    public PrintStream append(final CharSequence csq) {
//                        System.out.println("线程名=’" + Thread.currentThread().getName() + "‘，" +
//                                "CharSequence=’" + csq + "'");
//                        return this;
//                    }
//                })
//                .withEchoOutput(System.err)
//                .withInputFilters(removeColors(), removeNonPrintable())
//                .withExceptionOnFailure()
//                .build();

        try {
            expect.expect(sequence(contains("@"), contains(":")));
            expect.sendLine("pwd");
            expect.expect(sequence(contains("@"), contains(":")));
            expect.sendLine("docker ps");

            expect.expect(contains("@"));
            expect.sendLine("docker run -it  ubuntu-emacs-man /bin/bash");

            expect.expect(sequence(contains("@")));
            expect.sendLine("ls");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            expect.expect(sequence(contains("@")));
            expect.sendLine("cat /etc/passwd");

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            expect.expect(4000, sequence(contains("@")));   //not work
            expect.expect(sequence(contains("@")));   //not work
            expect.sendLine();
        } finally {
            expect.close();
            channel.disconnect();
            session.disconnect();
        }
    }
}