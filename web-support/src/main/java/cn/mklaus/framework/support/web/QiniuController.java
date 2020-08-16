package cn.mklaus.framework.support.web;

import cn.mklaus.framework.support.qiniu.Qiniu;
import cn.mklaus.framework.web.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author klausxie
 * @date 2020-08-16
 */
@RestController
@RequestMapping("/api/qiniu")
public class QiniuController {

    private final Qiniu qiniu;

    public QiniuController(Qiniu qiniu) {
        this.qiniu = qiniu;
    }

    @GetMapping("token")
    public Response qiniu() {
        return Response.ok()
                .put("action", qiniu.getQiniuProperties().getAction())
                .put("token", qiniu.getUploadToken())
                .put("bucket_url", qiniu.getQiniuProperties().getBucketUrl());
    }

}
