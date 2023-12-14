package com.niwxxs.modules;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Niwxxs
 * @create 2023-12-14-下午3:42
 */
public class FileManagementModule {

    private String path;

    public String uploadFile(File sourceFile) throws IOException {
        if (Objects.isNull(sourceFile)) {
            throw new NullPointerException("上传的文件不能为空");
        }
        final String sourceName = sourceFile.getName();
        final String suffix = sourceName.substring(sourceName.lastIndexOf(".") + 1);
        final FileInputStream is = new FileInputStream(sourceFile);
        final String fid = RandomStringUtils.random(15);
        final File targetFile = new File(path, fid.concat(".").concat(suffix));
        targetFile.createNewFile();
        FileCopyUtils.copy(is, new FileOutputStream(targetFile));
        return fid;
    }

    public File downloadFile(String fid) throws IOException {
        File targetFile = null;
        if (StringUtils.isEmpty(fid)) {
            throw new NullPointerException("下载的文件ID不能为空");
        }
        final File targetFileParent = new File(path);
        for (File file : targetFileParent.listFiles()) {
            if (file.getName().indexOf(fid) >= 0) {
                targetFile = file;
            }
        }
        if (Objects.isNull(targetFile)) {
            throw new FileNotFoundException("文件不存在");
        }
        return targetFile;
    }

}
