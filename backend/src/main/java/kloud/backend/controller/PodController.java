package kloud.backend.controller;

import io.kubernetes.client.openapi.ApiException;
import kloud.backend.service.PodService;
import kloud.backend.service.dto.KPodInfo;
import kloud.backend.service.dto.podCreate.PodCreateParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pod")
public class PodController {
    @Resource
    private PodService podService;

    @CrossOrigin
    @GetMapping("/listAll")
    public ResponseEntity<List<KPodInfo>> listAll() {
        return new ResponseEntity<>(podService.listAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/list")
    public ResponseEntity<List<KPodInfo>> listUser(@RequestParam String id, @RequestParam String course) {
        return new ResponseEntity<>(podService.listUser(id, course), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/create")
    public ResponseEntity<Void> create(@RequestBody PodCreateParam podCreateParam) {
        int result = podService.create(podCreateParam);
        if (result != 200) {
            return new ResponseEntity<>(HttpStatus.valueOf(result));
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/delete")
    public ResponseEntity<String> delete(@RequestBody Map<String, String> param) {
        String podName = param.get("podName");
        if (podName == null) {
            return new ResponseEntity<>("Missing param \"podName\"", HttpStatus.BAD_REQUEST);
        }
        String id = param.get("id");
        if (id == null) {
            return new ResponseEntity<>("Missing param \"id\"", HttpStatus.BAD_REQUEST);
        }
        try {
            String result = podService.delete(podName, id, param.get("course"));
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ApiException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getResponseBody(), HttpStatus.valueOf(e.getCode()));
        }
    }

    @CrossOrigin

    @GetMapping("/logt")
    public ResponseEntity<String> log(@RequestParam("id") String id, @RequestParam String podName, @RequestParam String course) {
        try {

            String log = podService.log(podName, id, course);
            return new ResponseEntity<>(log, HttpStatus.OK);
        } catch (ApiException e) {
            return new ResponseEntity<>(HttpStatus.valueOf(e.getCode()));
        }
    }

}
