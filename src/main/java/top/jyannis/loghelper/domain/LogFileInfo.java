package top.jyannis.loghelper.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Jyannis
 * @version 1.0 update on 2021/5/26
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LogFileInfo {

    /**
     * 文件大小 单位KB
     * file size (KB)
     */
    private Long fileSize;
    private String fileName;

}
