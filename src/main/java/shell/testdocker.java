package shell;


import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;

/**
 * @author liango
 * @version 1.0
 * @since 2018-04-26 2:24
 */
public class testdocker {

    public static void main(String[] args) {
        DockerClient dockerClient = (DockerClient) DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerHost("tcp://192.168.99.100:2376").build();

    }
}
