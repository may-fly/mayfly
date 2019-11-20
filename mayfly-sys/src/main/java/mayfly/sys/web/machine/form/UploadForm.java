package mayfly.sys.web.machine.form;

import lombok.Getter;
import lombok.Setter;
import mayfly.core.validation.annotation.NotBlank;
import mayfly.core.validation.annotation.NotNull;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author meilin.huang
 * @version 1.0
 * @date 2019-11-20 1:35 下午
 */
@Getter
@Setter
public class UploadForm {

    @NotNull
    private Integer fileId;

    @NotBlank
    private String path;

    private MultipartFile file;
}
