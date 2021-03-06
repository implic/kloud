package kloud.backend.service.dto;

import io.kubernetes.client.custom.ContainerMetrics;
import io.kubernetes.client.custom.PodMetrics;
import io.kubernetes.client.openapi.models.V1Pod;
import kloud.backend.config.Constants;
import lombok.Data;

import java.util.List;
import java.util.Objects;
@Data
public class KPodInfo {

    private String name;
    private String image;
    private String status;
    //    private int cpuUsage;
    private int memUsage;

    public KPodInfo(V1Pod v1Pod) {
        name = Objects.requireNonNull(v1Pod.getMetadata()).getName();
        image = Objects.requireNonNull(v1Pod.getSpec()).getContainers().get(0).getImage();
        status = Objects.requireNonNull(v1Pod.getStatus()).getPhase();
    }

    public KPodInfo(V1Pod v1Pod, PodMetrics metrics) {
        this(v1Pod);
        if (status != null && status.equals("Running")) {
//            cpuUsage = (int) metrics.getContainers().get(0).getUsage().get(CPU).getNumber().doubleValue();
            List<ContainerMetrics> list = metrics.getContainers();
            //不一定有在运行的容器？
            if (!list.isEmpty()) {
                double memUsage = list.get(0).getUsage().get(Constants.MEMORY).getNumber().doubleValue();
                this.memUsage = (int) (memUsage / Constants.MiB);
            }
        }
    }

}
