package mayfly.sys.module.machine.controller.form;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-20 1:35 下午
 */
@Getter
@Setter
@ToString
public class UploadForm {

    @NotNull
    private Long fileId;

    @NotBlank
    private String path;

    private MultipartFile file;
}
